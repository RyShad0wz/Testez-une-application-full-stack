describe('Sessions flow', () => {
  const nonAdmin = {
    token: 'fake-jwt',
    type: 'Bearer',
    id: 42,
    username: 'user42',
    firstName: 'First',
    lastName: 'Last',
    admin: false,
  };

  const admin = {
    token: 'fake-jwt',
    type: 'Bearer',
    id: 1,
    username: 'admin',
    firstName: 'Admin',
    lastName: 'User',
    admin: true,
  };

  /**
   * Login + stub GET /api/session
   */
  function doLogin(alias: string, user: any, sessionsList: any[]) {
    cy.intercept('GET', '**/api/session', {
      statusCode: 200,
      body: sessionsList,
    }).as('getList');
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: user,
    }).as(alias);

    cy.visit('/login');
    cy.get('[formControlName="email"]').type('foo@bar.com');
    cy.get('[formControlName="password"]').type('pwd1234');
    cy.get('button[type="submit"]').click();

    cy.wait(`@${alias}`);
    cy.wait('@getList');
    cy.url().should('include', '/sessions');
  }

  context('Non-admin user', () => {
    it('List page – shows sessions but no Create/Edit buttons', () => {
      const list = [
        { id: 1, name: 'Morning Yoga', description: 'Relaxing', date: '2025-03-01', teacher_id: 2, users: [] },
        { id: 2, name: 'Evening Yoga', description: 'Wind down', date: '2025-03-02', teacher_id: 2, users: [42] },
      ];
      doLogin('loginNA', nonAdmin, list);

      cy.get('.items mat-card.item').should('have.length', 2);
      cy.contains('button', 'Create').should('not.exist');
      cy.get('.items mat-card.item').first().within(() => {
        cy.contains('button', 'Detail').should('be.visible');
        cy.contains('button', 'Edit').should('not.exist');
      });
    });

    it('Detail page – participate & un-participate', () => {
      // 1) login + list stub
      doLogin('loginNA2', nonAdmin, [{ id: 1 }]);

      // 2) dynamic stub GET detail : 1er appel = [], 2e appel = [42], 3e appel = []
      let calls = 0;
      cy.intercept('GET', '**/api/session/1', (req) => {
        calls++;
        req.reply({
          statusCode: 200,
          body: {
            id: 1,
            name: 'Morning Yoga',
            description: 'Relax',
            date: '2025-03-01',
            teacher_id: 2,
            users: calls === 2 ? [42] : [],
          },
        });
      }).as('getDetail');

      // 3) stub du prof
      cy.intercept('GET', '**/api/teacher/2', {
        statusCode: 200,
        body: { id: 2, firstName: 'Teach', lastName: 'Er' },
      }).as('getTeacher');

      // 4) stub participantes
      cy.intercept('POST', '**/api/session/1/participate/42', {}).as('participate');
      cy.intercept('DELETE', '**/api/session/1/participate/42', {}).as('unparticipate');

      // 5) on clique Detail
      cy.get('.items mat-card.item').first().contains('button', 'Detail').click();
      cy.wait('@getDetail');
      cy.wait('@getTeacher');

      // 6) participer
      cy.contains('button', 'Participate').should('be.visible').click();
      cy.wait('@participate');
      cy.wait('@getDetail');
      cy.contains('button', 'Do not participate').should('be.visible').click();

      // 7) se désinscrire
      cy.wait('@unparticipate');
      cy.wait('@getDetail');
      cy.contains('button', 'Participate').should('be.visible');
    });
  });

  context('Admin user', () => {
    const listA = [
      { id: 1, name: 'Admin Session', description: 'Secret', date: '2025-07-01', teacher_id: 2, users: [] },
    ];
    const teachers = [
      { id: 2, firstName: 'Teach', lastName: 'Er' },
    ];

    it('List page – shows Create/Edit buttons', () => {
      doLogin('loginA1', admin, listA);

      cy.contains('button', 'Create').should('be.visible');
      cy.get('.items mat-card.item').first().within(() => {
        cy.contains('button', 'Detail').should('be.visible');
        cy.contains('button', 'Edit').should('be.visible');
      });
    });

    it('Detail page – delete session', () => {
      doLogin('loginA2', admin, [{ id: 1 }]);

      cy.intercept('GET', '**/api/session/1', {
        statusCode: 200,
        body: { id: 1, name: 'Admin Session', description: 'Secret', date: '2025-07-01', teacher_id: 2, users: [] },
      }).as('getDetailA');
      cy.intercept('GET', '**/api/teacher/2', teachers[0]).as('getTeacherA');
      cy.intercept('DELETE', '**/api/session/1', {}).as('deleteSessionA');

      cy.get('.items mat-card.item').first().contains('button', 'Detail').click();
      cy.wait('@getDetailA');
      cy.wait('@getTeacherA');

      cy.contains('button', 'Delete').click();
      cy.wait('@deleteSessionA');
      cy.contains('Session deleted !').should('be.visible');
      cy.url().should('include', '/sessions');
    });

    it('Create session form', () => {
      doLogin('loginA3', admin, listA);

      cy.intercept('GET', '**/api/teacher', teachers).as('getTeachersA');
      cy.intercept('POST', '**/api/session', {
        statusCode: 201,
        body: { id: 99, name: 'New', description: 'Brand new', date: '2025-08-01', teacher_id: 2, users: [] },
      }).as('createA');

      cy.contains('button', 'Create').click();
      cy.wait('@getTeachersA');

      cy.get('[formControlName="name"]').type('New');
      cy.get('[formControlName="date"]').type('2025-08-01');
      cy.get('mat-select').click();
      cy.contains('mat-option', 'Teach Er').click();
      cy.get('[formControlName="description"]').type('Brand new');
      cy.get('button[type="submit"]').click();

      cy.wait('@createA');
      cy.contains('Session created !').should('be.visible');
      cy.url().should('include', '/sessions');
    });

    it('Update session form', () => {
      doLogin('loginA4', admin, listA);

      cy.intercept('GET', '**/api/session/1', {
        statusCode: 200,
        body: { id: 1, name: 'Old session', description: 'Old', date: '2025-09-01', teacher_id: 2, users: [] },
      }).as('getDetailA2');
      cy.intercept('GET', '**/api/teacher', teachers).as('getTeachersA2');
      cy.intercept('PUT', '**/api/session/1', {
        statusCode: 200,
        body: { id: 1, name: 'Updated', description: 'New', date: '2025-09-02', teacher_id: 2, users: [] },
      }).as('updateA');

      cy.get('.items mat-card.item').first().contains('button', 'Edit').click();
      cy.wait('@getDetailA2');
      cy.wait('@getTeachersA2');

      cy.get('[formControlName="name"]').clear().type('Updated');
      cy.get('[formControlName="date"]').clear().type('2025-09-02');
      cy.get('mat-select').click();
      cy.contains('mat-option', 'Teach Er').click();
      cy.get('[formControlName="description"]').clear().type('New');
      cy.get('button[type="submit"]').click();

      cy.wait('@updateA');
      cy.contains('Session updated !').should('be.visible');
      cy.url().should('include', '/sessions');
    });
  });

  //
  // Garde / redirections pour non-admin
  //
  context('Guards & navigation pour non-admin', () => {
    beforeEach(() => {
      // on stub le login + liste vide
      cy.intercept('POST', '**/api/auth/login', { statusCode: 200, body: nonAdmin }).as('loginG');
      cy.intercept('GET', '**/api/session', []).as('getEmpty');
      // login + fetch list
      doLogin('loginG', nonAdmin, []);
    });

    it('Guard /sessions/create → /login', () => {
      // on tente un visit direct, on doit se retrouver sur /login
      cy.visit('/sessions/create', { failOnStatusCode: false });
      cy.url().should('include', '/login');
    });

    it('Guard /sessions/update/:id → /login', () => {
      cy.visit('/sessions/update/123', { failOnStatusCode: false });
      cy.url().should('include', '/login');
    });
  });


  context('Detail & back button pour admin', () => {
    beforeEach(() => {
      doLogin('loginB', admin, [{ id: 1 }]);
      cy.intercept('GET', '**/api/session/1', {
        statusCode: 200,
        body: { id: 1, name: 'Detay', description: 'Desc', date: '2025-01-01', teacher_id: 2, users: [] },
      }).as('getDetailB');
      cy.intercept('GET', '**/api/teacher/2', { id: 2, firstName: 'T', lastName: 'E' }).as('getTeacherB');
    });

    it('Back button calls history.back()', () => {
      cy.get('.items mat-card.item').first().contains('button', 'Detail').click();
      cy.wait('@getDetailB');
      cy.wait('@getTeacherB');

      cy.window().then(win => cy.stub(win.history, 'back').as('spyBack'));
      cy.get('button[mat-icon-button]').click();
      cy.get('@spyBack').should('have.been.calledOnce');
    });
  });
});

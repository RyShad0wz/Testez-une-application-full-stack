// cypress/e2e/sessions.cy.ts

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
  
    beforeEach(() => {
      cy.clearCookies();
      cy.clearLocalStorage();
    });
  
    function doLogin(alias: string, user: any) {
      cy.intercept('POST', '**/api/auth/login', {
        statusCode: 200,
        body: user
      }).as(alias);
  
      cy.visit('/login');
      cy.get('[formControlName="email"]').type('foo@bar.com');
      cy.get('[formControlName="password"]').type('pwd1234');
      cy.get('button[type="submit"]').click();
      cy.wait(`@${alias}`);
      cy.url().should('include', '/sessions');
    }
  
    context('Non-admin user', () => {
      const sessionsList = [
        { id: 1, name: 'Morning Yoga', description: 'Relaxing', date: '2025-03-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: '' },
        { id: 2, name: 'Evening Yoga', description: 'Wind down', date: '2025-03-02T00:00:00.000Z', teacher_id: 2, users: [42], createdAt: '', updatedAt: '' },
      ];
  
      it('List page – shows sessions but no Create/Edit buttons', () => {
        cy.intercept('GET', '**/api/session', sessionsList).as('getList');
        doLogin('loginReq', nonAdmin);
        cy.wait('@getList');
  
        cy.get('.items mat-card.item').should('have.length', 2);
        cy.contains('button', 'Create').should('not.exist');
        cy.get('.items mat-card.item').first().within(() => {
          cy.contains('button', 'Detail').should('be.visible');
          cy.contains('button', 'Edit').should('not.exist');
        });
      });
  
      it('Detail page – allows participate & un-participate', () => {
        cy.intercept('GET', '**/api/session', [{ id: 1 }]).as('getList');
        cy.intercept('GET', '**/api/session/1', {
          id: 1, name: 'Morning Yoga', description: 'Relax',
          date: '2025-03-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
        }).as('getDetail');
        cy.intercept('GET', '**/api/teacher/2', {
          id: 2, firstName: 'Teach', lastName: 'Er', createdAt: '', updatedAt: ''
        }).as('getTeacher');
        cy.intercept('POST', '**/api/session/1/participate/42', {}).as('participate');
        cy.intercept('DELETE', '**/api/session/1/participate/42', {}).as('unparticipate');
  
        doLogin('loginReq', nonAdmin);
        cy.wait('@getList');
  
        cy.visit('/sessions/detail/1');
        cy.wait('@getDetail');
        cy.wait('@getTeacher');
  
        cy.contains('button', 'Participate').click();
        cy.wait('@participate');
        cy.wait('@getDetail');
        cy.contains('button', 'Do not participate').should('be.visible');
  
        cy.contains('button', 'Do not participate').click();
        cy.wait('@unparticipate');
        cy.wait('@getDetail');
        cy.contains('button', 'Participate').should('be.visible');
      });
    });
  
    context('Admin user', () => {
      const sessionsAdminList = [
        { id: 1, name: 'Admin Session', description: 'Secret', date: '2025-07-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: '' }
      ];
      const teachersList = [
        { id: 2, firstName: 'Teach', lastName: 'Er', createdAt: '', updatedAt: '' }
      ];
  
      it('List page – shows Create/Edit buttons', () => {
        cy.intercept('GET', '**/api/session', sessionsAdminList).as('getList');
        doLogin('loginReq', admin);
        cy.wait('@getList');
  
        cy.contains('button', 'Create').should('be.visible');
        cy.get('.items mat-card.item').first().within(() => {
          cy.contains('button', 'Detail').should('be.visible');
          cy.contains('button', 'Edit').should('be.visible');
        });
      });
  
      it('Detail page – delete session', () => {
        cy.intercept('GET', '**/api/session', [{ id: 1 }]).as('getList');
        cy.intercept('GET', '**/api/session/1', {
          id: 1, name: 'Admin Session', description: 'Secret',
          date: '2025-07-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
        }).as('getDetail');
        cy.intercept('GET', '**/api/teacher/2', teachersList[0]).as('getTeacher');
        cy.intercept('DELETE', '**/api/session/1', {}).as('deleteSession');
  
        doLogin('loginReq', admin);
        cy.wait('@getList');
  
        cy.visit('/sessions/detail/1');
        cy.wait('@getDetail');
        cy.wait('@getTeacher');
  
        cy.contains('button', 'Delete').click();
        cy.wait('@deleteSession');
        cy.contains('Session deleted !').should('be.visible');
        cy.url().should('include', '/sessions');
      });
  
      it('Create session form', () => {
        cy.intercept('GET', '**/api/session', sessionsAdminList).as('getList');
        cy.intercept('GET', '**/api/teacher', teachersList).as('getTeachers');
        cy.intercept('POST', '**/api/session', {
          id: 99, name: 'New Session', description: 'Brand new',
          date: '2025-08-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
        }).as('createSession');
  
        doLogin('loginReq', admin);
        cy.wait('@getList');
  
        cy.visit('/sessions/create');
        cy.wait('@getTeachers');
  
        cy.get('[formControlName="name"]').type('New Session');
        cy.get('[formControlName="date"]').type('2025-08-01');
        cy.get('mat-select').click();
        cy.contains('mat-option', 'Teach Er').click();
        cy.get('[formControlName="description"]').type('Brand new');
        cy.get('button[type="submit"]').click();
  
        cy.wait('@createSession');
        cy.contains('Session created !').should('be.visible');
        cy.url().should('include', '/sessions');
      });
  
      it('Update session form', () => {
        cy.intercept('GET', '**/api/session', sessionsAdminList).as('getList');
        cy.intercept('GET', '**/api/teacher', teachersList).as('getTeachers');
        cy.intercept('GET', '**/api/session/123', {
          id: 123, name: 'Old Session', description: 'Old desc',
          date: '2025-09-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
        }).as('getDetail');
        cy.intercept('PUT', '**/api/session/123', {
          id: 123, name: 'Updated Session', description: 'New desc',
          date: '2025-09-02T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
        }).as('updateSession');
  
        doLogin('loginReq', admin);
        cy.wait('@getList');
  
        cy.visit('/sessions/update/123');
        cy.wait('@getDetail');
        cy.wait('@getTeachers');
  
        cy.get('[formControlName="name"]').clear().type('Updated Session');
        cy.get('[formControlName="date"]').clear().type('2025-09-02');
        cy.get('mat-select').click();
        cy.contains('mat-option', 'Teach Er').click();
        cy.get('[formControlName="description"]').clear().type('New desc');
        cy.get('button[type="submit"]').click();
  
        cy.wait('@updateSession');
        cy.contains('Session updated !').should('be.visible');
        cy.url().should('include', '/sessions');
      });
    });
  
  
    //
    // Nouveaux tests pour obtenir >80% de couverture sur FormComponent, DetailComponent et services
    //
  
    context('Guards & navigation pour non-admin', () => {
      beforeEach(() => {
        // stub login + liste vide
        cy.intercept('POST', '**/api/auth/login', { statusCode: 200, body: nonAdmin }).as('loginNA');
        cy.intercept('GET', '**/api/session', []).as('getEmpty');
        doLogin('loginNA', nonAdmin);
        cy.wait('@getEmpty');
      });
  
      it('Redirige de /sessions/create vers /sessions pour non-admin', () => {
        cy.visit('/sessions/create');
        cy.url().should('include', '/sessions');
      });
  
      it('Redirige de /sessions/update/123 vers /sessions pour non-admin', () => {
        cy.visit('/sessions/update/123');
        cy.url().should('include', '/sessions');
      });
    });
  
    context('Detail & bouton back pour admin', () => {
      beforeEach(() => {
        // stub login + liste + detail + teacher
        cy.intercept('POST', '**/api/auth/login', { statusCode: 200, body: admin }).as('loginA');
        cy.intercept('GET', '**/api/session', [{ id: 1 }]).as('getListA');
        cy.intercept('GET', '**/api/session/1', {
          id: 1,
          name: 'Admin Detay',
          description: 'Desc',
          date: '2025-01-01T00:00:00.000Z',
          teacher_id: 2,
          users: [],
          createdAt: '',
          updatedAt: ''
        }).as('getDetA');
        cy.intercept('GET', '**/api/teacher/2', { id: 2, firstName: 'T', lastName: 'E', createdAt: '', updatedAt: '' }).as('getTeachA');
  
        doLogin('loginA', admin);
        cy.wait('@getListA');
      });
  
      it('Affiche detail via /sessions/detail/:id et teste back()', () => {
        cy.visit('/sessions/detail/1');
        cy.wait('@getDetA');
        cy.wait('@getTeachA');
  
        cy.contains('h1', 'Admin Detay').should('be.visible');
  
        cy.window().then(win => cy.stub(win.history, 'back').as('historySpy'));
        cy.contains('button', 'Back').click();
        cy.get('@historySpy').should('have.been.calledOnce');
      });
    });
  });
  
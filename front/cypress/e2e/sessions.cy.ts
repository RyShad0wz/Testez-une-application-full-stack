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
  
    function doLogin(loginAlias: string, user: any) {
      cy.intercept('POST', '**/api/auth/login', {
        statusCode: 200,
        body: user
      }).as(loginAlias);
  
      cy.visit('/login');
      cy.get('[formControlName="email"]').type('foo@bar.com');
      cy.get('[formControlName="password"]').type('pwd1234');
      cy.get('button[type="submit"]').click();
      cy.wait(`@${loginAlias}`);
      cy.url().should('include', '/sessions');
    }
  
    context('Non-admin user', () => {
      const sessionsList = [
        { id: 1, name: 'Morning Yoga', description: 'Relaxing', date: '2025-03-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: '' },
        { id: 2, name: 'Evening Yoga', description: 'Wind down', date: '2025-03-02T00:00:00.000Z', teacher_id: 2, users: [42], createdAt: '', updatedAt: '' },
      ];
  
      it('List page – shows sessions but no Create/Edit buttons', () => {
        cy.intercept('GET', '**/api/session', {
          statusCode: 200,
          body: sessionsList
        }).as('getListNA');
  
        doLogin('loginNA', nonAdmin);
        cy.wait('@getListNA');
  
        cy.get('.items mat-card.item').should('have.length', 2);
        cy.contains('button', 'Create').should('not.exist');
        cy.get('.items mat-card.item').first().within(() => {
          cy.contains('button', 'Detail').should('be.visible');
          cy.contains('button', 'Edit').should('not.exist');
        });
      });
  
      it('Detail page – allows participate & un-participate', () => {
        cy.intercept('GET', '**/api/session', [{ id: 1 }]).as('getListNA2');
        cy.intercept('GET', '**/api/session/1', {
          statusCode: 200,
          body: {
            id: 1,
            name: 'Morning Yoga',
            description: 'Relax',
            date: '2025-03-01T00:00:00.000Z',
            teacher_id: 2,
            users: [],
            createdAt: '',
            updatedAt: ''
          }
        }).as('getDetailNA');
        cy.intercept('GET', '**/api/teacher/2', {
          statusCode: 200,
          body: { id: 2, firstName: 'Teach', lastName: 'Er', createdAt: '', updatedAt: '' }
        }).as('getTeacherNA');
        cy.intercept('POST', '**/api/session/1/participate/42', {}).as('participateNA');
        cy.intercept('DELETE', '**/api/session/1/participate/42', {}).as('unparticipateNA');
  
        doLogin('loginNA2', nonAdmin);
        cy.wait('@getListNA2');
  
        // on clique sur "Detail" pour aller à la page qui appellera getDetail et getTeacher
        cy.get('.items mat-card.item').first().contains('button', 'Detail').click();
        cy.wait('@getDetailNA');
        cy.wait('@getTeacherNA');
  
        // participer
        cy.contains('button', 'Participate').click();
        cy.wait('@participateNA');
        cy.wait('@getDetailNA');
        cy.contains('button', 'Do not participate').should('be.visible');
  
        // se désinscrire
        cy.contains('button', 'Do not participate').click();
        cy.wait('@unparticipateNA');
        cy.wait('@getDetailNA');
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
        cy.intercept('GET', '**/api/session', sessionsAdminList).as('getListA');
        doLogin('loginA1', admin);
        cy.wait('@getListA');
  
        cy.contains('button', 'Create').should('be.visible');
        cy.get('.items mat-card.item').first().within(() => {
          cy.contains('button', 'Detail').should('be.visible');
          cy.contains('button', 'Edit').should('be.visible');
        });
      });
  
      it('Detail page – delete session', () => {
        cy.intercept('GET', '**/api/session', [{ id: 1 }]).as('getListA2');
        cy.intercept('GET', '**/api/session/1', {
          statusCode: 200,
          body: {
            id: 1, name: 'Admin Session', description: 'Secret',
            date: '2025-07-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
          }
        }).as('getDetailA');
        cy.intercept('GET', '**/api/teacher/2', teachersList[0]).as('getTeacherA');
        cy.intercept('DELETE', '**/api/session/1', {}).as('deleteSessionA');
  
        doLogin('loginA2', admin);
        cy.wait('@getListA2');
  
        cy.get('.items mat-card.item').first().contains('button', 'Detail').click();
        cy.wait('@getDetailA');
        cy.wait('@getTeacherA');
  
        cy.contains('button', 'Delete').click();
        cy.wait('@deleteSessionA');
        cy.contains('Session deleted !').should('be.visible');
        cy.url().should('include', '/sessions');
      });
  
      it('Create session form', () => {
        cy.intercept('GET', '**/api/session', sessionsAdminList).as('getListA3');
        cy.intercept('GET', '**/api/teacher', teachersList).as('getTeachersA');
        cy.intercept('POST', '**/api/session', {
          statusCode: 201,
          body: {
            id: 99, name: 'New Session', description: 'Brand new',
            date: '2025-08-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
          }
        }).as('createSessionA');
  
        doLogin('loginA3', admin);
        cy.wait('@getListA3');
  
        // on clique sur "Create" pour aller au formulaire
        cy.contains('button', 'Create').click();
        cy.wait('@getTeachersA');
  
        cy.get('[formControlName="name"]').type('New Session');
        cy.get('[formControlName="date"]').type('2025-08-01');
        cy.get('mat-select').click();
        cy.contains('mat-option', 'Teach Er').click();
        cy.get('[formControlName="description"]').type('Brand new');
        cy.get('button[type="submit"]').click();
  
        cy.wait('@createSessionA');
        cy.contains('Session created !').should('be.visible');
        cy.url().should('include', '/sessions');
      });
  
      it('Update session form', () => {
        cy.intercept('GET', '**/api/session', sessionsAdminList).as('getListA4');
        cy.intercept('GET', '**/api/teacher', teachersList).as('getTeachersA2');
        cy.intercept('GET', '**/api/session/1', {
          statusCode: 200,
          body: {
            id: 1, name: 'Old Session', description: 'Old desc',
            date: '2025-09-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
          }
        }).as('getDetailA2');
        cy.intercept('PUT', '**/api/session/1', {
          statusCode: 200,
          body: {
            id: 1, name: 'Updated Session', description: 'New desc',
            date: '2025-09-02T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
          }
        }).as('updateSessionA');
  
        doLogin('loginA4', admin);
        cy.wait('@getListA4');
  
        cy.get('.items mat-card.item').first().within(() => {
          cy.contains('button', 'Edit').click();
        });
        cy.wait('@getDetailA2');
        cy.wait('@getTeachersA2');
  
        cy.get('[formControlName="name"]').clear().type('Updated Session');
        cy.get('[formControlName="date"]').clear().type('2025-09-02');
        cy.get('mat-select').click();
        cy.contains('mat-option', 'Teach Er').click();
        cy.get('[formControlName="description"]').clear().type('New desc');
        cy.get('button[type="submit"]').click();
  
        cy.wait('@updateSessionA');
        cy.contains('Session updated !').should('be.visible');
        cy.url().should('include', '/sessions');
      });
    });
  
    //
    // Garde / redirections pour non-admin 
    //
    context('Guards & navigation pour non-admin', () => {
      beforeEach(() => {
        cy.intercept('POST', '**/api/auth/login', { statusCode: 200, body: nonAdmin }).as('loginG');
        cy.intercept('GET', '**/api/session', []).as('getEmpty');
        doLogin('loginG', nonAdmin);
        cy.wait('@getEmpty');
      });
  
      it('Redirige de /sessions/create vers /sessions pour non-admin', () => {
        cy.visit('/sessions/create');
        cy.wait('@getEmpty');
        cy.url().should('include', '/sessions');
      });
  
      it('Redirige de /sessions/update/123 vers /sessions pour non-admin', () => {
        cy.visit('/sessions/update/123');
        cy.wait('@getEmpty');
        cy.url().should('include', '/sessions');
      });
    });
  
    //
    // Test du bouton "Back" dans le detail pour admin
    //
    context('Detail & bouton back pour admin', () => {
      beforeEach(() => {
        cy.intercept('POST', '**/api/auth/login', { statusCode: 200, body: admin }).as('loginB');
        cy.intercept('GET', '**/api/session', [{ id: 1 }]).as('getListB');
        cy.intercept('GET', '**/api/session/1', {
          id: 1, name: 'Admin Detay', description: 'Desc',
          date: '2025-01-01T00:00:00.000Z', teacher_id: 2, users: [], createdAt: '', updatedAt: ''
        }).as('getDetailB');
        cy.intercept('GET', '**/api/teacher/2', { id: 2, firstName: 'T', lastName: 'E', createdAt: '', updatedAt: '' }).as('getTeacherB');
  
        doLogin('loginB', admin);
        cy.wait('@getListB');
      });
  
      it('Affiche detail via bouton Detail et teste back()', () => {
        cy.get('.items mat-card.item').first().contains('button', 'Detail').click();
        cy.wait('@getDetailB');
        cy.wait('@getTeacherB');
  
        // on espionne history.back()
        cy.window().then(win => cy.stub(win.history, 'back').as('spyBack'));
        cy.contains('button', 'Back').click();
        cy.get('@spyBack').should('have.been.calledOnce');
      });
    });
  });
  
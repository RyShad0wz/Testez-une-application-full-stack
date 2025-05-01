// cypress/e2e/login.cy.ts
describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login');

    // Stub dynamique du login
    cy.intercept('POST', '**/api/auth/login', (req) => {
      const { email, password } = req.body;
      if (email === 'yoga@studio.com' && password === 'test!1234') {
        req.reply({
          statusCode: 200,
          body: {
            token: 'fake-jwt',
            type: 'Bearer',
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
          }
        });
      } else if (email && password) {
        req.reply({ statusCode: 401, body: { message: 'Bad credentials' } });
      } else {
        req.reply({ statusCode: 500, body: { message: 'Internal server error' } });
      }
    }).as('loginDynamic');

    // Stub pour sessions
    cy.intercept('GET', '**/api/session', { statusCode: 200, body: [] }).as('getSession');
  });

  it('Login successful', () => {
    cy.get('[formControlName="email"]').type('yoga@studio.com');
    cy.get('[formControlName="password"]').type('test!1234');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginDynamic');
    cy.wait('@getSession');
    cy.url().should('include', '/sessions');
  });

  it('Toggles password visibility when clicking the eye icon', () => {
    // Empêche le eye-button de soumettre le formulaire
    cy.get('button[matSuffix]').invoke('attr', 'type', 'button');

    cy.get('[formControlName="password"]').should('have.attr', 'type', 'password');
    cy.get('button[matSuffix]').click();
    cy.get('[formControlName="password"]').should('have.attr', 'type', 'text');
    cy.get('button[matSuffix]').click();
    cy.get('[formControlName="password"]').should('have.attr', 'type', 'password');
  });

  it('Shows error on bad credentials (401)', () => {
    cy.get('[formControlName="email"]').type('foo@bar.com');
    cy.get('[formControlName="password"]').type('wrong');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginDynamic');
    cy.get('p.error').should('be.visible').and('contain', 'An error occurred');
  });
});

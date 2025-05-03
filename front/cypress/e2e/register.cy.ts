describe('Register spec', () => {
  const validFirstName  = '5';        // passes Validators.min(3) numerically
  const validLastName   = '5';
  const validPassword   = '5';        // same for password
  const validEmail      = `test${Date.now()}@example.com`;

  it('Submit button is disabled when form is invalid', () => {
    cy.visit('/register');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Register successful', () => {
    cy.intercept('POST', '**/api/auth/register', {
      statusCode: 200,
      body: {}
    }).as('registerRequest');

    cy.visit('/register');
    cy.get('[formControlName="firstName"]').type(validFirstName);
    cy.get('[formControlName="lastName"]').type(validLastName);
    cy.get('[formControlName="email"]').type(validEmail);
    cy.get('[formControlName="password"]').type(validPassword);

    cy.get('button[type="submit"]')
      .should('not.be.disabled')
      .click();

    cy.wait('@registerRequest').its('response.statusCode').should('eq', 200);
    cy.url().should('include', '/login');
  });

  it('Shows generic error on failure', () => {
    cy.intercept('POST', '**/api/auth/register', {
      statusCode: 500,
      body: { message: 'Error occurred' }
    }).as('registerRequest');

    cy.visit('/register');
    cy.get('[formControlName="firstName"]').type(validFirstName);
    cy.get('[formControlName="lastName"]').type(validLastName);
    cy.get('[formControlName="email"]').type(validEmail);
    cy.get('[formControlName="password"]').type(validPassword);

    cy.get('button[type="submit"]').should('not.be.disabled').click();
    cy.wait('@registerRequest');

    cy.get('span.error')
      .should('be.visible')
      .and('contain', 'An error occurred');
  });
});

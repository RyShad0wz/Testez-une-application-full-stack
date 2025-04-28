describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register');
    
    // Vérifie que les champs existent
    cy.get('input[formControlName=firstName]').should('exist');
    cy.get('input[formControlName=lastName]').should('exist');
    cy.get('input[formControlName=email]').should('exist');
    cy.get('input[formControlName=password]').should('exist');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {}
    }).as('registerRequest');

    const testEmail = `toto${Math.floor(Math.random() * 10000)}@toto.com`;
    
    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    cy.get('input[formControlName=email]').type(testEmail);
    cy.get('input[formControlName=password]').type('test!1234{enter}');

    cy.wait('@registerRequest').its('response.statusCode').should('eq', 200);
    cy.url().should('include', '/login');
  });

  it('Register with error', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500,
      body: { message: 'Error occurred' }
    }).as('registerRequest');

    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    cy.get('input[formControlName=email]').type('toto3@toto.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}');

    cy.wait('@registerRequest');
    cy.get('.error').should('be.visible')
      .and('contain', 'An error occurred'); // Adaptez au message réel
  });
});
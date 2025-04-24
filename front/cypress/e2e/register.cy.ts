describe('Register spec', () => {
    it('Register successfull', () => {
      // 1. Visite la page d'inscription
      cy.visit('/register');
  
      // 2. Intercepte la requête POST vers /api/auth/register
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
        body: {}
      }).as('registerRequest');
  
      // 3. Remplit le formulaire
      cy.get('input[formControlName=firstName]').type('toto');
      cy.get('input[formControlName=lastName]').type('toto');
      cy.get('input[formControlName=email]').type('toto3@toto.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}');
  
      // 4. Vérifie la redirection vers /login
      cy.url().should('include', '/login');
    });
  
    it('Register with error', () => {
      // 1. Visite la page d'inscription
      cy.visit('/register');
  
      // 2. Intercepte la requête avec une erreur
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 500,
        body: {}
      }).as('registerRequest');
  
      // 3. Remplit le formulaire
      cy.get('input[formControlName=firstName]').type('toto');
      cy.get('input[formControlName=lastName]').type('toto');
      cy.get('input[formControlName=email]').type('toto3@toto.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}');
  
      // 4. Vérifie que l'erreur s'affiche
      cy.get('.error').should('be.visible');
    });
  });
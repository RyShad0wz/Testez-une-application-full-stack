describe('NotFound spec', () => {
  it('Shows 404 page on bad route', () => {
    cy.visit('/this-route-does-not-exist', { failOnStatusCode: false });
    cy.contains(/Page not found/i).should('be.visible');
  });
});

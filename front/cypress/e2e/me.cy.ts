// cypress/e2e/me.cy.ts

describe('Me spec', () => {
  const userId     = 42
  const loginUrl   = '**/api/auth/login'
  const sessionUrl = '**/api/session'
  const userUrl    = `**/api/user/${userId}`

  /**
   * Connecte l’utilisateur en UI pour peupler SessionService
   */
  function uiLogin(admin = false) {
    cy.intercept('POST', loginUrl, {
      statusCode: 200,
      body: {
        token: 'fake-jwt',
        type: 'Bearer',
        id: userId,
        username: 'foo',
        firstName: 'First',
        lastName: 'Last',
        admin
      }
    }).as('loginRequest')

    cy.intercept('GET', sessionUrl, { statusCode: 200, body: [] }).as('getSession')

    cy.visit('/login')
    cy.get('[formControlName="email"]').type('foo@bar.com')
    cy.get('[formControlName="password"]').type('pwd1234')
    cy.get('button[type="submit"]').click()

    cy.wait('@loginRequest')
    cy.wait('@getSession')
    cy.url().should('include', '/sessions')
  }

  it('Redirects to login if not authenticated', () => {
    cy.visit('/me', { failOnStatusCode: false })
    cy.url().should('include', '/login')
  })

  context('when logged in as non-admin', () => {
    beforeEach(() => {
      uiLogin(false)
    })

    it('Displays user info and delete button', () => {
      // 1) stub de la récupération du user
      cy.intercept('GET', userUrl, {
        statusCode: 200,
        body: {
          id: userId,
          firstName: 'First',
          lastName: 'Last',
          email: 'first.last@example.com',
          admin: false,
          createdAt: '2025-01-01T00:00:00.000Z',
          updatedAt: '2025-04-01T00:00:00.000Z'
        }
      }).as('getUser')

      // 2) stub de la suppression
      cy.intercept('DELETE', userUrl, { statusCode: 204 }).as('deleteUser')

      // 3) navigation vers la page /me via le menu
      cy.get('span.link').contains('Account').click()

      // 4) on attend l’appel userService.getById()
      cy.wait('@getUser')

      // 5) vérifications du profil
      cy.contains('Name: First LAST').should('be.visible')
      cy.contains('Email: first.last@example.com').should('be.visible')
      cy.contains('Delete my account:').should('be.visible')

      // 6) suppression et snack-bar
      cy.get('button[color="warn"]').click()
      cy.wait('@deleteUser')
      cy.get('simple-snack-bar')
        .should('contain', 'Your account has been deleted !')

      // 7) redirection vers la home page
      cy.url().should('eq', Cypress.config().baseUrl)
    })
  })

  context('when logged in as admin', () => {
    beforeEach(() => {
      uiLogin(true)
    })

    it('Displays admin message and no delete button', () => {
      cy.intercept('GET', userUrl, {
        statusCode: 200,
        body: {
          id: userId,
          firstName: 'First',
          lastName: 'Last',
          email: 'first.last@example.com',
          admin: true,
          createdAt: '2025-01-01T00:00:00.000Z',
          updatedAt: '2025-04-01T00:00:00.000Z'
        }
      }).as('getUserAdmin')

      cy.get('span.link').contains('Account').click()
      cy.wait('@getUserAdmin')

      cy.contains('Name: First LAST').should('be.visible')
      cy.contains('Email: first.last@example.com').should('be.visible')
      cy.contains('You are admin').should('be.visible')
      cy.contains('Delete my account:').should('not.exist')
    })
  })
})

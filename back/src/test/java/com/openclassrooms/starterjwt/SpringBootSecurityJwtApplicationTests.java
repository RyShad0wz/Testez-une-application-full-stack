package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

	@Test
	public void contextLoads() {
		// Ce test vérifie que le contexte Spring se charge correctement
	}

	@Test
	public void testMainMethod() {
		// Test de la méthode main
		SpringBootSecurityJwtApplication.main(new String[] {});

		// Si la méthode main fait plus que simplement lancer l'application,
		// vous devriez ajouter des assertions ici
	}

	@Test
	public void testConstructor() {
		// Bien que déjà couvert par contextLoads(), voici un test explicite
		SpringBootSecurityJwtApplication app = new SpringBootSecurityJwtApplication();
		assertNotNull(app);
	}
}

package test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import sources.hex.Pion;
import sources.hex.Plateau;

class PlateauTest {

	@Test
	void test() {
		final int taille = 4;
		int mode =1;
		Plateau p = new Plateau(taille,mode);
		assertEquals(taille, p.taille());
		
		System.out.println(p);
		assertEquals(
				" A B C D\n" + 
				"1 . . . .\n" + 
				"2  . . . .\n" + 
				"3   . . . .\n" + 
				"4    . . . .\n", p.toString());
		
	
		String pos = ".X..XOXXOO.OX...";
		
		String[] lignes;
		lignes  = Plateau.decouper(taille, pos);
		
		String[] lignes_rep = {
				".X..", 
				"XOXX",
				"OO.O",
				"X..."
		};
		for (int i = 0; i< taille; ++i)
			assertEquals(lignes_rep[i], lignes[i]);
		
		Plateau p1 = new Plateau(taille, pos);
		System.out.println(p1);
		assertEquals(
				" A B C D\n" + 
				"1 . X . .\n" + 
				"2  X O X X\n" + 
				"3   O O . O\n" + 
				"4    X . . .\n", p1.toString());
		
		
		assertEquals(5,p1.getNb(Pion.Croix));
		assertEquals(4,p1.getNb(Pion.Rond));
		
		
	}
	@Test
	public void testerGetCoup() {
		// Test getcoup
		int taille = 4;
				Plateau p = new Plateau(taille,1);
				assertEquals(taille, p.taille());	
				p.JouerTour("c3");
				p.JouerTour("c4");
				assertEquals(Pion.Croix, p.getCase("c3"));
				assertEquals(Pion.Rond, p.getCase("c4"));
				assertEquals(Pion.Vide, p.getCase("c1"));
				System.out.println(p);
	}

	@Test
	public void TestModeDeJeu() {
		int taille = 4;
		Plateau p = new Plateau(taille, 1);
		assertTrue(p.getJoueur1());
		assertTrue(p.getJoueur2());
		assertEquals(p.getMode(), 1);
		p=new Plateau(taille, 2);
		assertFalse(p.getJoueur1()); 
		assertTrue(p.getJoueur2());
		assertEquals(p.getMode(), 2);
		p=new Plateau(taille, 3);
		assertFalse(p.getJoueur1()); 
		assertFalse(p.getJoueur2());
		assertEquals(p.getMode(), 3);
	}
	@Test
	public void testChangerPion() {
		int taille = 6;
		Plateau p = new Plateau(taille, 1);
		assertEquals(p.getPionJoueur1(), Pion.Rond);
		assertEquals(p.getPionJoueur2(), Pion.Croix);
		p.JouerTour("b4");
		assertEquals(p.getCoups(), 1);
		assertTrue(p.ChangerPion());
		assertFalse(p.ChangerPion());
		assertEquals(p.getPionJoueur1(), Pion.Croix);
		assertEquals(p.getPionJoueur2(), Pion.Rond);
	}
}

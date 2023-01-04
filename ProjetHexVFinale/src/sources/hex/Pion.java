package sources.hex;

public enum Pion {
	Croix('X'), Rond('O'), Vide('.');
	private char symbole;
	private Pion (char symbole) {
		this.symbole = symbole;
	}
	@Override
	public String toString() {
		return ""+symbole;
	}
	public static Pion get(char c) {
		for (Pion p : Pion.values())
			if (p.symbole == c)
				return p;
		throw new IllegalArgumentException(
				"symbole inconnu " + c);
	}
}

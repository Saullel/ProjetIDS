package types;

public enum Case {
	VIDE (0),
	MUR (-1),
	JOUEUR (-2),
	OBSTACLE (-3);

	private final int valeur;

	Case(int valeur) {
		this.valeur = valeur;
	}
}

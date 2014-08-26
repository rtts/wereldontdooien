package nl.returntothesource.wereldontdooien.io;

/**
 * Created by jolandaverhoef on 19-01-14.
 */
public class Fonkel {
    public String afbeelding;
    public int type;

    public static String getTypeString(int type) {
        switch(type) {
            case 1: return "Spreuk";
            case 2: return "Tip";
            case 3: return "Persoonlijke opdracht";
            case 4: return "Opdracht in gezelschap";
            case 5: return "Kadootje";
            case 6: return "Complimentje";
            case 7: return "Doe-dingen";
            case 8: return "Recept";
            case 9: return "Foto-opdracht";
            case 10: return "Gedicht";
            default: return "Overig";
        }
    }
}

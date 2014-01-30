package nl.returntothesource.wereldontdooien;

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
            default: return "Overig";
        }
    }
}

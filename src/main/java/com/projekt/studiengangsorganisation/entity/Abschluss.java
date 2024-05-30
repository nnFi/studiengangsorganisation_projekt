package com.projekt.studiengangsorganisation.entity;

/**
 * Enumerationsklasse, die verschiedene Abschlussarten repräsentiert.
 */
public enum Abschluss {
    // Bachelorabschlüsse
    BACHELOR_OF_ARTS("Bachelor of Arts (B.A.)"),
    BACHELOR_OF_SCIENCE("Bachelor of Science (B.Sc.)"),
    BACHELOR_OF_ENGINEERING("Bachelor of Engineering (B.Eng.)"),
    BACHELOR_OF_LAWS("Bachelor of Laws (LL.B.)"),
    BACHELOR_OF_EDUCATION("Bachelor of Education (B.Ed.)"),
    BACHELOR_OF_FINE_ARTS("Bachelor of Fine Arts (B.F.A.)"),
    BACHELOR_OF_MUSIC("Bachelor of Music (B.Mus.)"),

    // Masterabschlüsse
    MASTER_OF_ARTS("Master of Arts (M.A.)"),
    MASTER_OF_SCIENCE("Master of Science (M.Sc.)"),
    MASTER_OF_ENGINEERING("Master of Engineering (M.Eng.)"),
    MASTER_OF_LAWS("Master of Laws (LL.M.)"),
    MASTER_OF_EDUCATION("Master of Education (M.Ed.)"),
    MASTER_OF_BUSINESS_ADMINISTRATION("Master of Business Administration (MBA)"),
    MASTER_OF_FINE_ARTS("Master of Fine Arts (M.F.A.)"),
    MASTER_OF_MUSIC("Master of Music (M.Mus.)"),

    // Diplomabschlüsse
    DIPLOM_INGENIEUR("Diplom-Ingenieur (Dipl.-Ing.)"),
    DIPLOM_KAUFMANN("Diplom-Kaufmann / Diplom-Kauffrau (Dipl.-Kfm. / Dipl.-Kffr.)"),
    DIPLOM_VOLKSWIRT("Diplom-Volkswirt (Dipl.-Vw.)"),
    DIPLOM_PSYCHOLOGE("Diplom-Psychologe (Dipl.-Psych.)"),
    DIPLOM_SOZIALPAEDAGOGE("Diplom-Sozialpädagoge (Dipl.-Soz.päd.)"),
    DIPLOM_SOZIOLOGE("Diplom-Soziologe (Dipl.-Soz.)"),

    // Magisterabschlüsse
    MAGISTER_ARTIUM("Magister Artium (M.A.)"),

    // Staatsexamen
    STAATSEXAMEN_ERSTES_LEHRAMT("Erstes Staatsexamen (Lehramt)"),
    STAATSEXAMEN_ZWEITES_LEHRAMT("Zweites Staatsexamen (Lehramt)"),
    STAATSEXAMEN_ERSTES_JURA("Erstes Staatsexamen (Jura)"),
    STAATSEXAMEN_ZWEITES_JURA("Zweites Staatsexamen (Jura)"),
    STAATSEXAMEN_MEDIZIN("Staatsexamen (Medizin)"),
    STAATSEXAMEN_PHARMAZIE("Staatsexamen (Pharmazie)"),
    STAATSEXAMEN_LEBENSMITTELCHEMIE("Staatsexamen (Lebensmittelchemie)"),

    // Promotionsabschlüsse
    DOKTOR_PHILOSOPHIE("Doktor der Philosophie (Dr. phil.)"),
    DOKTOR_NATURWISSENSCHAFTEN("Doktor der Naturwissenschaften (Dr. rer. nat.)"),
    DOKTOR_INGENIEURWISSENSCHAFTEN("Doktor der Ingenieurwissenschaften (Dr.-Ing.)"),
    DOKTOR_RECHTSWISSENSCHAFTEN("Doktor der Rechtswissenschaften (Dr. jur.)"),
    DOKTOR_WIRTSCHAFTSWISSENSCHAFTEN("Doktor der Wirtschaftswissenschaften (Dr. rer. pol.)"),
    DOKTOR_MEDIZIN("Doktor der Medizin (Dr. med.)"),
    DOKTOR_ZAHNMEDIZIN("Doktor der Zahnmedizin (Dr. med. dent.)"),
    DOKTOR_TIERMEDIZIN("Doktor der Tiermedizin (Dr. med. vet.)");

    private final String bezeichnung;

    /**
     * Konstruktor für Abschluss.
     * @param bezeichnung Die Bezeichnung des Abschlusses.
     */
    Abschluss(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    /**
     * Gibt die Bezeichnung des Abschlusses zurück.
     * @return Die Bezeichnung des Abschlusses.
     */
    public String getBezeichnung() {
        return bezeichnung;
    }
}
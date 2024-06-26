package com.projekt.studiengangsorganisation.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projekt.studiengangsorganisation.entity.Abschluss;
import com.projekt.studiengangsorganisation.entity.Admin;
import com.projekt.studiengangsorganisation.entity.Fachbereich;
import com.projekt.studiengangsorganisation.entity.Fachgruppe;
import com.projekt.studiengangsorganisation.entity.Mitarbeiter;
import com.projekt.studiengangsorganisation.entity.Modul;
import com.projekt.studiengangsorganisation.entity.ModulArt;
import com.projekt.studiengangsorganisation.entity.Modulgruppe;
import com.projekt.studiengangsorganisation.entity.Pruefung;
import com.projekt.studiengangsorganisation.entity.Pruefungsordnung;
import com.projekt.studiengangsorganisation.entity.Sprache;
import com.projekt.studiengangsorganisation.entity.Student;
import com.projekt.studiengangsorganisation.entity.Studiengang;
import com.projekt.studiengangsorganisation.repository.AdminRepository;
import com.projekt.studiengangsorganisation.repository.FachbereichRepository;
import com.projekt.studiengangsorganisation.repository.FachgruppeRepository;
import com.projekt.studiengangsorganisation.repository.MitarbeiterRepository;
import com.projekt.studiengangsorganisation.repository.ModulRepository;
import com.projekt.studiengangsorganisation.repository.ModulgruppeRepository;
import com.projekt.studiengangsorganisation.repository.PruefungRepository;
import com.projekt.studiengangsorganisation.repository.PruefungsordnungRepository;
import com.projekt.studiengangsorganisation.repository.StudentRepository;
import com.projekt.studiengangsorganisation.repository.StudiengangRepository;
import com.projekt.studiengangsorganisation.service.AdminService;
import com.projekt.studiengangsorganisation.service.FachbereichService;
import com.projekt.studiengangsorganisation.service.FachgruppeService;
import com.projekt.studiengangsorganisation.service.MitarbeiterService;
import com.projekt.studiengangsorganisation.service.ModulService;
import com.projekt.studiengangsorganisation.service.ModulgruppeService;
import com.projekt.studiengangsorganisation.service.NutzerService;
import com.projekt.studiengangsorganisation.service.PruefungService;
import com.projekt.studiengangsorganisation.service.PruefungsordnungService;
import com.projekt.studiengangsorganisation.service.StudentService;
import com.projekt.studiengangsorganisation.service.StudiengangService;

@RequestMapping("/api/testdata")
@RestController
public class TestDataController {

    @Autowired
    StudiengangService studiengangService;

    @Autowired
    FachgruppeService fachgruppeService;

    @Autowired
    FachbereichService fachbereichService;

    @Autowired
    MitarbeiterService mitarbeiterService;

    @Autowired
    StudentService studentService;

    @Autowired
    ModulgruppeService modulgruppeService;

    @Autowired
    ModulService modulService;

    @Autowired
    PruefungService pruefungService;

    @Autowired
    PruefungsordnungService pruefungsordnungService;

    @Autowired
    AdminService adminService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FachbereichRepository fachbereichRepository;

    @Autowired
    FachgruppeRepository fachgruppeRepository;

    @Autowired
    ModulgruppeRepository modulgruppeRepository;

    @Autowired
    ModulRepository modulRepository;

    @Autowired
    StudiengangRepository studiengangRepository;

    @Autowired
    PruefungsordnungRepository pruefungsordnungRepository;

    @Autowired
    PruefungRepository pruefungRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Erstellt Testdaten für die Anwendung.
     * Diese Methode löscht zunächst alle vorhandenen Daten und fügt dann
     * Testdaten für verschiedene Entitäten wie Admin, Mitarbeiter, Studenten,
     * Fachbereiche, Fachgruppen, Module und Prüfungen ein.
     *
     * @return Eine Bestätigungsnachricht, dass die Testdaten erfolgreich erstellt
     *         wurden.
     */
    @SuppressWarnings("unused")
    @PostMapping("")
    public String createTestData() {
        clearDatabase();
        Admin admin = insertTestAdmin();

        Mitarbeiter mitarbeiter1 = insertTestMitarbeiter1();
        Mitarbeiter mitarbeiter2 = insertTestMitarbeiter2();

        Student student = insertTestStudent();

        Fachbereich fachbereich = insertTestFachbereich(mitarbeiter1, mitarbeiter2);

        List<Fachgruppe> fachgruppen = insertTestFachgruppen(fachbereich, mitarbeiter1, mitarbeiter2);

        Modulgruppe modulgruppeMG = insertTestModulgruppeMG();

        Studiengang studiengang = insertTestStudiengang(mitarbeiter1, mitarbeiter2, fachbereich);

        Pruefungsordnung pruefungsordnung = insertTestDPO(studiengang);

        insertSemester1Modules(fachgruppen, mitarbeiter1, modulgruppeMG, pruefungsordnung);
        insertSemester2Modules(fachgruppen, mitarbeiter1, modulgruppeMG, pruefungsordnung);
        insertSemester3Modules(fachgruppen, mitarbeiter1, modulgruppeMG, pruefungsordnung);
        insertSemester4Modules(fachgruppen, mitarbeiter1, modulgruppeMG, pruefungsordnung);
        insertSemester5Modules(fachgruppen, mitarbeiter1, modulgruppeMG, pruefungsordnung);
        insertSemester6Modules(fachgruppen, mitarbeiter1, modulgruppeMG, pruefungsordnung);

        return "Testdaten wurden erfolgreich erstellt!";
    }

    /**
     * Fügt Testdaten für einen Admin ein.
     * 
     * @return Der erstellte Admin mit Testdaten.
     */
    public Admin insertTestAdmin() {
        Admin admin = new Admin();
        admin.setVorname("test");
        admin.setNachname("admin");
        admin.setPassword(passwordEncoder.encode("TestPassword123!"));
        admin.setUsername(admin.getVorname().toLowerCase() + "." + admin.getNachname().toLowerCase());

        adminRepository.saveAndFlush(admin);

        return admin;
    }

    /**
     * Fügt Testdaten für einen Mitarbeiter1 ein.
     * 
     * @return Der erstellte Mitarbeiter mit Testdaten.
     */
    public Mitarbeiter insertTestMitarbeiter1() {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname("test");
        mitarbeiter.setNachname("mitarbeiter1");
        mitarbeiter.setPassword(passwordEncoder.encode("TestPassword123!"));
        mitarbeiter.setUsername(mitarbeiter.getVorname().toLowerCase() + "." + mitarbeiter.getNachname().toLowerCase());

        mitarbeiterRepository.saveAndFlush(mitarbeiter);

        return mitarbeiter;
    }

    /**
     * Fügt Testdaten für einen Mitarbeiter2 ein.
     * 
     * @return Der erstellte Mitarbeiter mit Testdaten.
     */
    public Mitarbeiter insertTestMitarbeiter2() {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname("test");
        mitarbeiter.setNachname("mitarbeiter2");
        mitarbeiter.setPassword(passwordEncoder.encode("TestPassword123!"));
        mitarbeiter.setUsername(mitarbeiter.getVorname().toLowerCase() + "." + mitarbeiter.getNachname().toLowerCase());

        mitarbeiterRepository.saveAndFlush(mitarbeiter);

        return mitarbeiter;
    }

    /**
     * Fügt Testdaten für einen Studenten ein.
     * 
     * @return Der erstellte Student mit Testdaten.
     */
    public Student insertTestStudent() {
        Student student = new Student();
        student.setVorname("test");
        student.setNachname("student");
        student.setPassword(passwordEncoder.encode("TestPassword123!"));
        student.setUsername(student.getVorname().toLowerCase() + "." + student.getNachname().toLowerCase());

        studentRepository.saveAndFlush(student);

        return student;
    }

    /**
     * Fügt Testdaten für einen Fachbereich ein.
     * 
     * @param referent       Der Referent des Fachbereichs.
     * @param stellvertreter Der Stellvertreter des Fachbereichs.
     * @return Der erstellte Fachbereich mit Testdaten.
     */
    public Fachbereich insertTestFachbereich(Mitarbeiter referent, Mitarbeiter stellvertreter) {
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setName("Wirtschaft");
        fachbereich.setReferent(referent);
        fachbereich.setStellvertreter(stellvertreter);

        fachbereichRepository.saveAndFlush(fachbereich);

        return fachbereich;
    }

    /**
     * Fügt Testdaten für Fachgruppen ein.
     * 
     * @param fachbereich    Der Fachbereich, zu dem die Fachgruppen gehören.
     * @param referent       Der Referent der Fachgruppen.
     * @param stellvertreter Der Stellvertreter der Fachgruppen.
     * @return Eine Liste der erstellten Fachgruppen mit Testdaten.
     */
    public List<Fachgruppe> insertTestFachgruppen(Fachbereich fachbereich, Mitarbeiter referent,
            Mitarbeiter stellvertreter) {
        List<Fachgruppe> fachgruppen = Arrays.asList(
                createFachgruppe("Management", "MG", fachbereich, referent, stellvertreter),
                createFachgruppe("Controlling, Finanz- und Rechnungswesen", "CFR", fachbereich, referent,
                        stellvertreter),
                createFachgruppe("Mathematik und Statistik", "M/S", fachbereich, referent, stellvertreter),
                createFachgruppe("Wirtschaftsinformatik", "WI", fachbereich, referent, stellvertreter),
                createFachgruppe("Sprachen", "SP", fachbereich, referent, stellvertreter),
                createFachgruppe("Personal und Organisation", "P/O", fachbereich, referent, stellvertreter),
                createFachgruppe("Produktion und Logistik", "P/L", fachbereich, referent, stellvertreter),
                createFachgruppe("Recht", "RE", fachbereich, referent, stellvertreter));
        return fachgruppeRepository.saveAll(fachgruppen);
    }

    /**
     * Erstellt eine einzelne Fachgruppe mit den gegebenen Parametern.
     * 
     * @param name           Der Name der Fachgruppe.
     * @param kuerzel        Das Kürzel der Fachgruppe.
     * @param fachbereich    Der zugehörige Fachbereich.
     * @param referent       Der Referent der Fachgruppe.
     * @param stellvertreter Der Stellvertreter der Fachgruppe.
     * @return Die erstellte Fachgruppe.
     */
    private Fachgruppe createFachgruppe(String name, String kuerzel, Fachbereich fachbereich, Mitarbeiter referent,
            Mitarbeiter stellvertreter) {
        Fachgruppe fachgruppe = new Fachgruppe();
        fachgruppe.setName(name);
        fachgruppe.setKuerzel(kuerzel);
        fachgruppe.setFachbereich(fachbereich);
        fachgruppe.setReferent(referent);
        fachgruppe.setStellvertreter(stellvertreter);
        return fachgruppe;
    }

    /**
     * Fügt Testdaten für eine Modulgruppe ein.
     * 
     * @return Die erstellte Modulgruppe mit Testdaten.
     */
    public Modulgruppe insertTestModulgruppeMG() {
        Modulgruppe modulgruppe = new Modulgruppe();
        modulgruppe.setName("Modulgruppe 1");

        modulgruppeRepository.saveAndFlush(modulgruppe);

        return modulgruppe;
    }

    /**
     * Fügt Testdaten für einen Studiengang ein.
     * 
     * @param leiter         Der Leiter des Studiengangs.
     * @param stellvertreter Der stellvertretende Leiter des Studiengangs.
     * @param fachbereich    Der Fachbereich, zu dem der Studiengang gehört.
     * @return Der erstellte Studiengang mit Testdaten.
     */
    public Studiengang insertTestStudiengang(Mitarbeiter leiter, Mitarbeiter stellvertreter, Fachbereich fachbereich) {
        Studiengang studiengang = new Studiengang();
        studiengang.setName("Wirtschaftsinformatik");
        studiengang.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        studiengang.setRegelstudienzeit(6);
        studiengang.setLeiter(leiter);
        studiengang.setStellvertretenderLeiter(stellvertreter);
        studiengang.setFachbereich(fachbereich);

        studiengangRepository.saveAndFlush(studiengang);

        return studiengang;
    }

    /**
     * Fügt Testdaten für eine Prüfungsordnung ein.
     * 
     * @param studiengang Der Studiengang, zu dem die Prüfungsordnung gehört.
     * @return Die erstellte Prüfungsordnung mit Testdaten.
     */
    public Pruefungsordnung insertTestDPO(Studiengang studiengang) {
        Pruefungsordnung pruefungsordnung = new Pruefungsordnung();
        pruefungsordnung.setFreigegeben(true);
        pruefungsordnung.setVersion("17");
        pruefungsordnung.setAuslaufend(false);
        pruefungsordnung.setStudiengang(studiengang);

        pruefungsordnungRepository.saveAndFlush(pruefungsordnung);

        return pruefungsordnung;
    }

    /**
     * Fügt Testdaten für Module des ersten Semesters ein.
     * 
     * @param fachgruppen      Die Liste der verfügbaren Fachgruppen.
     * @param beauftragter     Der Modulbeauftragte.
     * @param modulgruppe      Die Modulgruppe, zu der die Module gehören.
     * @param pruefungsordnung Die zugehörige Prüfungsordnung.
     */
    public void insertSemester1Modules(List<Fachgruppe> fachgruppen, Mitarbeiter beauftragter, Modulgruppe modulgruppe,
            Pruefungsordnung pruefungsordnung) {
        insertModuleAndPruefung("BWL für Wirtschaftsinformatik", 150, 6, getFachgruppeByKuerzel(fachgruppen, "MG"),
                beauftragter, modulgruppe, pruefungsordnung, 1, 1);
        insertModuleAndPruefung("Externes Rechnungswesen und Finanzierung", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "CFR"), beauftragter, modulgruppe, pruefungsordnung, 2, 1);
        insertModuleAndPruefung("Mathematik für Wirtschaftsinformatik", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "M/S"), beauftragter, modulgruppe, pruefungsordnung, 3, 1);
        insertModuleAndPruefung("Grundlagen der Informatik", 300, 12, getFachgruppeByKuerzel(fachgruppen, "WI"),
                beauftragter, modulgruppe, pruefungsordnung, 4, 1);
    }

    /**
     * Fügt Testdaten für Module des zweiten Semesters ein.
     * 
     * @param fachgruppen      Die Liste der verfügbaren Fachgruppen.
     * @param beauftragter     Der Modulbeauftragte.
     * @param modulgruppe      Die Modulgruppe, zu der die Module gehören.
     * @param pruefungsordnung Die zugehörige Prüfungsordnung.
     */
    public void insertSemester2Modules(List<Fachgruppe> fachgruppen, Mitarbeiter beauftragter, Modulgruppe modulgruppe,
            Pruefungsordnung pruefungsordnung) {
        insertModuleAndPruefung("Materialwirtschaft und Produktionsplanung/-steuerung", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter, modulgruppe, pruefungsordnung, 5, 2);
        insertModuleAndPruefung("Internes Rechnungswesen und Investition", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "CFR"), beauftragter, modulgruppe, pruefungsordnung, 6, 2);
        insertModuleAndPruefung("Datenbanken", 150, 6, getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter,
                modulgruppe, pruefungsordnung, 7, 2);
        insertModuleAndPruefung("Systementwicklung", 300, 12, getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter,
                modulgruppe, pruefungsordnung, 8, 2);
    }

    /**
     * Fügt Testdaten für Module des dritten Semesters ein.
     * 
     * @param fachgruppen      Die Liste der verfügbaren Fachgruppen.
     * @param beauftragter     Der Modulbeauftragte.
     * @param modulgruppe      Die Modulgruppe, zu der die Module gehören.
     * @param pruefungsordnung Die zugehörige Prüfungsordnung.
     */
    public void insertSemester3Modules(List<Fachgruppe> fachgruppen, Mitarbeiter beauftragter, Modulgruppe modulgruppe,
            Pruefungsordnung pruefungsordnung) {
        insertModuleAndPruefung("Technologie von Enterprise Resource Planning-Systemen", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter, modulgruppe, pruefungsordnung, 9, 3);
        insertModuleAndPruefung("Netzwerke", 150, 6, getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter,
                modulgruppe, pruefungsordnung, 10, 3);
        insertModuleAndPruefung("Mathematik für Ökonomen", 150, 6, getFachgruppeByKuerzel(fachgruppen, "M/S"),
                beauftragter, modulgruppe, pruefungsordnung, 11, 3);
        insertModuleAndPruefung("Kommunikation und Projektmanagement", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter, modulgruppe, pruefungsordnung, 12, 3);
        insertModuleAndPruefung("Software Engineering", 150, 6, getFachgruppeByKuerzel(fachgruppen, "WI"),
                beauftragter, modulgruppe, pruefungsordnung, 13, 3);
    }

    /**
     * Fügt Testdaten für Module des vierten Semesters ein.
     * 
     * @param fachgruppen      Die Liste der verfügbaren Fachgruppen.
     * @param beauftragter     Der Modulbeauftragte.
     * @param modulgruppe      Die Modulgruppe, zu der die Module gehören.
     * @param pruefungsordnung Die zugehörige Prüfungsordnung.
     */
    public void insertSemester4Modules(List<Fachgruppe> fachgruppen, Mitarbeiter beauftragter, Modulgruppe modulgruppe,
            Pruefungsordnung pruefungsordnung) {
        insertModuleAndPruefung("Betriebssysteme und IT-Sicherheit", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter, modulgruppe, pruefungsordnung, 14, 4);
        insertModuleAndPruefung("Web-Technologien", 150, 6, getFachgruppeByKuerzel(fachgruppen, "WI"),
                beauftragter, modulgruppe, pruefungsordnung, 15, 4);
        insertModuleAndPruefung("Softwareprojekt", 150, 6, getFachgruppeByKuerzel(fachgruppen, "WI"),
                beauftragter, modulgruppe, pruefungsordnung, 16, 4);
        insertModuleAndPruefung("Seminar zur Wirtschaftsinformatik", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter, modulgruppe, pruefungsordnung, 17, 4);
    }

    /**
     * Fügt Testdaten für Module des fünften Semesters ein.
     * 
     * @param fachgruppen      Die Liste der verfügbaren Fachgruppen.
     * @param beauftragter     Der Modulbeauftragte.
     * @param modulgruppe      Die Modulgruppe, zu der die Module gehören.
     * @param pruefungsordnung Die zugehörige Prüfungsordnung.
     */
    public void insertSemester5Modules(List<Fachgruppe> fachgruppen, Mitarbeiter beauftragter, Modulgruppe modulgruppe,
            Pruefungsordnung pruefungsordnung) {
        insertModuleAndPruefung("Englisch für Wirtschaftsinformatik", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "SP"), beauftragter, modulgruppe, pruefungsordnung, 18, 5);
        insertModuleAndPruefung("Konzepte und Technologien im E-Commerce", 150, 6,
                getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter, modulgruppe, pruefungsordnung, 19, 5);
        insertModuleAndPruefung("Projekt zur Wirtschaftsinformatik", 300, 12,
                getFachgruppeByKuerzel(fachgruppen, "WI"), beauftragter, modulgruppe, pruefungsordnung, 20, 5);
    }

    /**
     * Fügt Testdaten für Module des sechsten Semesters ein.
     * 
     * @param fachgruppen      Die Liste der verfügbaren Fachgruppen.
     * @param beauftragter     Der Modulbeauftragte.
     * @param modulgruppe      Die Modulgruppe, zu der die Module gehören.
     * @param pruefungsordnung Die zugehörige Prüfungsordnung.
     */
    public void insertSemester6Modules(List<Fachgruppe> fachgruppen, Mitarbeiter beauftragter, Modulgruppe modulgruppe,
            Pruefungsordnung pruefungsordnung) {
        insertModuleAndPruefung("Praxisphase", 450, 18, getFachgruppeByKuerzel(fachgruppen, "WI"),
                beauftragter, modulgruppe, pruefungsordnung, 21, 6);
        insertModuleAndPruefung("Bachelor-Arbeit", 300, 12, getFachgruppeByKuerzel(fachgruppen, "WI"),
                beauftragter, modulgruppe, pruefungsordnung, 22, 6);
    }

    /**
     * Sucht eine Fachgruppe anhand ihres Kürzels aus einer Liste von Fachgruppen.
     * 
     * @param fachgruppen Die Liste der Fachgruppen, in der gesucht werden soll.
     * @param kuerzel     Das Kürzel der gesuchten Fachgruppe.
     * @return Die gefundene Fachgruppe.
     * @throws RuntimeException wenn keine Fachgruppe mit dem angegebenen Kürzel
     *                          gefunden wurde.
     */
    private Fachgruppe getFachgruppeByKuerzel(List<Fachgruppe> fachgruppen, String kuerzel) {
        return fachgruppen.stream()
                .filter(fg -> fg.getKuerzel().equals(kuerzel))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fachgruppe mit Kürzel " + kuerzel + " nicht gefunden"));
    }

    /**
     * Fügt ein neues Modul und die zugehörige Prüfung in die Datenbank ein.
     * 
     * @param modulName        Der Name des Moduls.
     * @param workload         Der Workload des Moduls in Stunden.
     * @param credits          Die Anzahl der Credits für das Modul.
     * @param fachgruppe       Die zugehörige Fachgruppe.
     * @param beauftragter     Der Modulbeauftragte.
     * @param modulgruppe      Die Modulgruppe, zu der das Modul gehört.
     * @param pruefungsordnung Die zugehörige Prüfungsordnung.
     * @param pruefungsnummer  Die Nummer der Prüfung.
     * @param fachsemester     Das Fachsemester, in dem das Modul angeboten wird.
     */
    private void insertModuleAndPruefung(String modulName, int workload, int credits, Fachgruppe fachgruppe,
            Mitarbeiter beauftragter,
            Modulgruppe modulgruppe, Pruefungsordnung pruefungsordnung, int pruefungsnummer, int fachsemester) {
        Modul modul = new Modul();
        modul.setName(modulName);
        modul.setWorkload(workload);
        modul.setAbschluss(Abschluss.BACHELOR_OF_SCIENCE);
        modul.setArt(ModulArt.VORLESUNG);
        modul.setBeschreibung("Beschreibung des Moduls " + modulName);
        modul.setCredits(credits);
        modul.setDauer(1);
        modul.setFreigegeben(true);
        modul.setSprache(Sprache.DEUTSCH);
        modul.setFachgruppe(fachgruppe);
        modul.setModulbeauftragter(beauftragter);
        modul.setModulgruppe(modulgruppe);

        Modul savedModul = modulService.saveAndFlush(modul);
        String modulnummer = (fachgruppe.getFachbereich().getId() + " " + fachgruppe.getKuerzel() + " "
                + savedModul.getId());
        savedModul.setModulnummer(modulnummer);
        modulService.saveAndFlush(savedModul);

        Pruefung pruefung = new Pruefung();
        pruefung.setPruefungsnummer(pruefungsnummer);
        pruefung.setFachsemester(fachsemester);
        pruefung.setPruefungsordnung(pruefungsordnung);
        pruefung.setModul(savedModul);
        Pruefung savedPruefung = pruefungRepository.saveAndFlush(pruefung);

        Set<Pruefung> pruefungen = new HashSet<>(Arrays.asList(savedPruefung));
        savedModul.setPruefungen(pruefungen);
        modulService.saveAndFlush(savedModul);
    }

    /**
     * Löscht alle vorhandenen Daten aus der Datenbank.
     * Diese Methode wird aufgerufen, bevor neue Testdaten eingefügt werden.
     */
    public void clearDatabase() {
        pruefungRepository.deleteAll();
        modulRepository.deleteAll();
        pruefungsordnungRepository.deleteAll();
        studiengangRepository.deleteAll();
        modulgruppeRepository.deleteAll();
        fachgruppeRepository.deleteAll();
        fachbereichRepository.deleteAll();
        mitarbeiterRepository.deleteAll();
        adminRepository.deleteAll();
    }
}
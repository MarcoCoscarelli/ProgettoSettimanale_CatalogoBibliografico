package Catalogo;

import Catalogo.Dao.PrestitoDao;
import Catalogo.Dao.PubblicazioneDao;
import Catalogo.Dao.UtenteDao;
import Catalogo.Entity.Prestito;
import Catalogo.Entity.Pubblicazione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("catalogo");
        EntityManager em = emf.createEntityManager();


        PubblicazioneDao pubblicazioneDao = new PubblicazioneDao(em);
        UtenteDao utenteDao = new UtenteDao(em);
        PrestitoDao prestitoDao = new PrestitoDao(em);

        try {
            em.getTransaction().begin();


            Pubblicazione pubblicazione = pubblicazioneDao.searchByIsbn("97888227362");
            if (pubblicazione != null) {
                Prestito nuovoPrestito = new Prestito();
                nuovoPrestito.setUtente(utenteDao.getById(1));
                nuovoPrestito.setElementoPrestato(pubblicazione);
                nuovoPrestito.setDataInizioPrestito(LocalDate.now());
                nuovoPrestito.setDataRestituzioneEffettiva(LocalDate.now().plusDays(30));


                prestitoDao.save(nuovoPrestito);
                System.out.println("Prestito creato: " + nuovoPrestito);
            }


            int annoPubblicazione = 1980;
            List<Pubblicazione> lista1 = pubblicazioneDao.searchByYearPublishing(annoPubblicazione);
            System.out.println("Pubblicazioni dell'anno " + annoPubblicazione + ":");
            lista1.forEach(System.out::println);


            System.out.println("Prestiti scaduti e non restituiti:");
            List<Prestito> lista5 = prestitoDao.getPrestitiScadutiNonRestituiti();
            lista5.forEach(System.out::println);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}

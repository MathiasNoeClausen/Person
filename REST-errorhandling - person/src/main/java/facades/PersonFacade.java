package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import utils.IPersonFacade;

public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {
    }

    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void main(String[] args) {
        insertData();
    }

    public static void insertData() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        Person p1 = new Person("Mathias", "Clausen", "12345678");
        Person p2 = new Person("Gustav", "Kristensen", "12345678");
        Person p3 = new Person("Rasmus", "Ulstrup", "12345678");
        
        Address a1 = new Address("Lyngbyvej 30", 1234, "Lyngby");
        Address a2 = new Address("Eremitageparken", 4321, "Lyngby");
        Address a3 = new Address("Valmuevej", 2341, "Helsingør");
        
        p1.setAddress(a3);
        p2.setAddress(a2);
        p3.setAddress(a1);
        
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.getTransaction().commit();
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) {
        EntityManager em = emf.createEntityManager();
        Person person = new Person(fName, lName, phone);
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
        return new PersonDTO(person);

    }

    @Override
    
    public PersonDTO deletePerson(long id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if(person == null) {
            throw new PersonNotFoundException(String.format("Person with id: (%id) not found", id));
        }
        try {
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO getPerson(long id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if(person == null) {
            throw new PersonNotFoundException(String.format("Person with id: (%d) not found", person.getId()));
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, p.getId());
        if(person == null) {
            throw new PersonNotFoundException(String.format("Person with id: (%d) not found", p.getId()));
        }
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setPhone(p.getPhone());
        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
            
            return new PersonDTO(person);
        } finally {
            em.close();
        }
        
        
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createNamedQuery("Person.getAll", Person.class);
            PersonsDTO persons = new PersonsDTO(query.getResultList());
            return persons;
        } finally {
            
        }
        
    }

}

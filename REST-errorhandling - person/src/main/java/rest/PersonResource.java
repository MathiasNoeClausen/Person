package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("person")
public class PersonResource {
//private PersonDTO aPerson = FACADE.addPerson("Mikkel", "Clausen", "12345678");



    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    
    private static final PersonFacade FACADE =  PersonFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getById(@PathParam("id") long id) throws PersonNotFoundException {
        PersonDTO personById = FACADE.getPerson(id);
        String jsonPerson = GSON.toJson(personById);
        
        return personById;
    }
//    @Path("ny")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getPerson() {
//        return GSON.toJson(aPerson);
//    }
//   
//   @POST
//   @Consumes(MediaType.APPLICATION_JSON)
//   @Produces(MediaType.APPLICATION_JSON)
//   public Response addPerson(String person) {
//       aPerson = GSON.fromJson(person, PersonDTO.class);
//       return Response.ok(aPerson).build();
//   } 
    
    
//    Hvis disse 2 endpoints ikke er udkommenteret virker hele mit program ikke, synes ellers de ser rigtige nok ud 
   // Går ud fra at at den forsøger at bruge både get, put og delete på samme tid/id og derfor crasher. 
//   @Path("/{id}")
//   @DELETE
//   @Produces(MediaType.APPLICATION_JSON)
//   public String deletePerson(@PathParam("id") long id) throws PersonNotFoundException {
//       PersonDTO deletePersonById = FACADE.deletePerson(id);
//       
//       return GSON.toJson(deletePersonById);
//   }
//   
//   @Path("/{id")
//   @PUT
//   @Consumes(MediaType.APPLICATION_JSON)
//   @Produces(MediaType.APPLICATION_JSON)
//   public String updatePerson(@PathParam("id") long id, String person) throws PersonNotFoundException {
//       PersonDTO pDTO = GSON.fromJson(person, PersonDTO.class);
//       pDTO.setId(id);
//       PersonDTO pNy = FACADE.editPerson(pDTO);
//       return GSON.toJson(pNy);
//   }
   @Path("all")
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public String getAll() {
       PersonsDTO persons = FACADE.getAllPersons();
       return GSON.toJson(persons);
   }
    
    }


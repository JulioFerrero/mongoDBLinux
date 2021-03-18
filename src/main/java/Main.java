import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception  {
        boolean fin = false;
        while (!fin) {
            System.out.println("1. Lista las bases de datos de MongoDB");
            System.out.println("2. Lista las colecciones de una base de datos");
            System.out.println("3. Visualiza todos los documentos de profesores");
            System.out.println("4. Inserta un nuevo documento profesor");
            System.out.println("5. Inserta un documento profesor con un objeto aula");
            System.out.println("6. Modifica un documento profesor");
            System.out.println("7. Borra un documento profesor por id");
            System.out.println("8. Borra todos los documentos profesor");
            System.out.println("9. Consulta con alguna función de agregación");
            System.out.println("10. Vuelca todos los documentos en un fichero de texto");
            System.out.println("11. Salir");
            System.out.print("> ");

            Scanner sc = new Scanner(System.in);
            int input = sc.nextInt();

            ConnectionString connString = new ConnectionString(
                    "mongodb+srv://dbUser:jak1k0@cluster0.cjxmj.mongodb.net/?retryWrites=true&w=majority"
            );

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .retryWrites(true)
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase("nueva");
            MongoCollection<Document> collection = database.getCollection("profes");


            switch (input) {
                case 1:
                    List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
                    databases.forEach(db -> System.out.println(db.toJson()));

                    /*for (String s : mongoClient.listDatabaseNames()) {
                        System.out.println(s);
                    }*/
                    break;
                case 2:
                    MongoDatabase db = mongoClient.getDatabase("nueva");
                    for (String name : db.listCollectionNames()) {
                        System.out.println(name);
                    }
                    break;
                case 3:
                    for (Document doc : collection.find()) {
                        System.out.println(doc.toJson());
                    }
                    break;
                case 4:
                    System.out.print("Nombre: ");
                    String nombre = sc.next();

                    System.out.print("Edad: ");
                    String edad = sc.next();

                    Document profesor = new Document();
                    profesor.put("nombre",nombre);
                    profesor.put("Edad", edad);
                    collection.insertOne(profesor);
                    break;
                case 5:
                    System.out.print("Nombre: ");
                    nombre = sc.next();

                    System.out.print("Edad: ");
                    edad = sc.next();

                    System.out.print("Clase: ");
                    String claseInput = sc.next();

                    System.out.print("nivel: ");
                    String nivel = sc.next();

                    Document mainWithObject = new Document();
                    mainWithObject.put("Nombre", nombre);
                    mainWithObject.put("Edad", edad);
                    Document clase = new Document();
                    clase.put("nombre", claseInput);
                    clase.put("nivel", nivel);
                    mainWithObject.put("clase", clase);
                    collection.insertOne(mainWithObject);
                    break;
                case 6:
                    System.out.print("id: ");
                    String id = sc.next();

                    System.out.print("nuevo nombre: ");
                    String name = sc.next();

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(id));
                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.put("nombre", name);
                    BasicDBObject updateObject = new BasicDBObject();
                    updateObject.put("$set", newDocument);
                    collection.updateOne(query,updateObject);
                    break;
                case 7:
                    System.out.print("id: ");
                    id = sc.next();

                    collection.deleteOne(new Document("_id",new ObjectId(id)));
                    break;
                case 8:
                    BasicDBObject document = new BasicDBObject();
                    collection.deleteMany(document);
                    break;
                case 9:
                    System.out.println("Mucha pereza");
                    break;
                case 10:
                    String sPath = "backup.json";
                    StringBuilder contentBackup = new StringBuilder();
                    contentBackup.append("{\"backup\" : [");
                    Path path = Paths.get(sPath);
                    Files.deleteIfExists(path);
                    Files.createFile(path);

                    FileWriter write = new FileWriter(sPath);

                    for (Document doc : collection.find()) {
                        contentBackup.append(doc.toJson()).append(",");
                    }

                    contentBackup.deleteCharAt(contentBackup.length()-1);
                    String rawBackup = String.valueOf(contentBackup.append("]}"));

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

                    JsonNode tree = objectMapper .readTree(rawBackup);
                    String formattedBackup = objectMapper.writeValueAsString(tree);

                    write.write(formattedBackup);
                    write.close();
                    break;
                case 11:
                    fin = true;
                    break;
                default:
                    System.out.println("ERROR");
                    break;
            }
        }
    }
}

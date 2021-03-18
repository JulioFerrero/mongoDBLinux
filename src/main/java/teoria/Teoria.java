package teoria;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//hetose4828@fironia.com


public class Teoria {
    public static void main(String[] args) {

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

        collection.deleteOne(new Document("_id",1));
        collection.deleteOne(new Document("telefono",97887));
        collection.deleteOne(new Document("_id",7));
        collection.deleteOne(new Document("_id",8));
        collection.deleteOne(new Document("_id",9));
        collection.deleteOne(new Document("_id",10));

        //Mediante el método insertOne se inserta  un documento en la colección.
        Document amigo = new Document();
        amigo.put("_id",1);
        amigo.put("nombre","Pedro");
        amigo.put("edad", 30);
        collection.insertOne(amigo);

        //Inserción de un documento sin id y con fecha.
        Document agenda = new Document();
        agenda.put("nombre","Jose");
        agenda.put("telefono", 97887);
        agenda.put("curso","DAM2");
        agenda.put("fecha", new Date());
        collection.insertOne(agenda);

        //Inserción de un objeto dentro de un documento.
        Document mainWithObject = new Document();
        mainWithObject.put("_id",7);
        mainWithObject.put("nombre", "Maria");
        mainWithObject.put("telefono", 444);
        Document clase = new Document();
        clase.put("nombre", "Informatica");
        clase.put("nivel", "DAM2");
        mainWithObject.put("clase", clase);
        collection.insertOne(mainWithObject);

        //Inserción de un array dentro de un documento.
        Document mainWithArray = new Document();
        mainWithArray.put("_id",8);
        mainWithArray.put("nombre", "Pilar");
        mainWithArray.put("telefono", 678944);
        List <String> lista = new ArrayList<>();
        lista.add("dao");
        lista.add("java");
        mainWithArray.put("Modulos", lista);
        collection.insertOne(mainWithArray);

        //Inserción de un array de objetos dentro de un documento.
        Document mainWithDBL = new Document();
        mainWithDBL.put("_id", 9);
        mainWithDBL.put("nombre", "Juan");
        mainWithDBL.put("telefono", 678944);
        BasicDBList dbList = new BasicDBList();
        dbList.add(new BasicDBObject("informatica","DAM2"));
        dbList.add(new BasicDBObject("fol", "ASIX1"));
        mainWithDBL.put("modulos",dbList);
        collection.insertOne(mainWithDBL);

        //Inserción mezcla de array y un dato normal.
        Document mainWithArrayandObject = new Document();
        mainWithArrayandObject.put("_id",10);
        mainWithArrayandObject.put("nombre", "Lucas");
        mainWithArrayandObject.put("telefono", 4489082);
        BasicDBList dbList1 = new BasicDBList();
        dbList1.add(new BasicDBObject("informatica", "DAM2"));
        dbList1.add(new BasicDBObject("fol", "ASIX1"));
        BasicDBObject outer = new BasicDBObject("institut", "ies").append("items",dbList1);
        mainWithArrayandObject.put("modulos",outer);
        collection.insertOne(mainWithArrayandObject);

        //Modificación de un documento.
        BasicDBObject query = new BasicDBObject();
        query.put("_id",3);
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("nombre", "Mateo");
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);
        collection.updateOne(query,updateObject);

        //Modificación de muchos documentos.
        BasicDBObject searchQuery = new BasicDBObject();
        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.append("$set", new BasicDBObject().append("edad", 100));
        collection.updateMany(searchQuery, updateQuery);



        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            System.out.println(doc.toJson());
        }

        //Borrar todo.
        BasicDBObject document = new BasicDBObject();
        collection.deleteMany(document);

        cursor.close();
    }
}

# mongoDB guide for GNU+Linux with free Atlas
The creators of MongoDB grant a free server to be able to use their technology, in our case we are going to take advantage of it to learn and do tests.

First we must register [on their website](https://www.mongodb.com/cloud/atlas):
![First image](/images/007.png)

We give our project a name and specify the language that we are going to use (in our case Java):
![Second Image](/images/010.png)

We choose the free plan:
![Third image](/images/013.png)

We select the server provider and the location:
![Fourth image](/images/016.png)

We just have to wait for it to finish starting and we can use our new server:
![Fifth image](/images/019.png)

That's all friends! we add our beautiful IP in the white list and we can access it through Java or Mongo Shell:
![Sixth image](/images/025.png)

If you want to learn how to connect through java, take a look at the code in this repository but here is some sample code.
But be careful, to start using mongo in Java we must install its JAR, in my case through POM.xml
```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.2.0</version>
</dependency>
```

Okay! First we have to add the ConnectionString of our server:
```java
ConnectionString connString = new ConnectionString(
                    "mongodb+srv://USERINMYSERVER:PASSWORD@URLOFMYSERVER/?retryWrites=true&w=majority"
            );
```

In my case it would be like this:
```java
ConnectionString connString = new ConnectionString(
                "mongodb+srv://dbUser:jak1k0@cluster0.cjxmj.mongodb.net/?retryWrites=true&w=majority"
        );
```
We add the options with MongoClientSettings:
```java
MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
```
And we apply them:
```java
MongoClient mongoClient = MongoClients.create(settings);
```
And finally we specify the database and the collection:
```java
MongoDatabase database = mongoClient.getDatabase("nueva");
MongoCollection<Document> collection = database.getCollection("profes");
```
Y con esto y un bizcocho (as we say in Catalonia) we would already have it, we can use `collection.deleteOne` to delete or `collection.insertOne` to add a new document.
If you want to learn more, you have some code examples from this repository or [in the MongoDB documentation](https://docs.mongodb.com/manual/reference/method/).
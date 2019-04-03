# RoomRxJava
Room Rxjava example shows how to fetch data as observable from database using room so that observable not only emits data but also emits data as it changes in the database. 

I could not find a SQLite query that would insert or update without causing unwanted changes to my foreign key, so instead I opted to insert first, ignoring conflicts if they occurred, and updating immediately afterwards, again ignoring conflicts.

The insert and update methods are protected so external classes see and use the upsert method only. Keep in mind that this isn't a true upsert as if any of the MyEntity POJOS have null fields, they will overwrite what may currently be in the database. This is not a caveat for me, but it may be for your application.

```java
@Insert(onConflict = OnConflictStrategy.IGNORE)
void insert(List<MyEntity> entities);

@Update(onConflict = OnConflictStrategy.IGNORE)
void update(List<MyEntity> entities);
```
```java
public void upsert(List<MyEntity> entities) {
    insert(entities);
    update(entities);
}
```


# Libraries
Room, RxJava2.

# Links
I learned a lot from this article:
 
 https://medium.com/google-developers/room-rxjava-acb0cd4f3757
 
 https://proandroiddev.com/android-room-handling-relations-using-livedata-2d892e40bd53
 

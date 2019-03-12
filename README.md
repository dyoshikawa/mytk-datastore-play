# Migration

```
./migrate create -ext sql -dir conf/db/migration/default create_tasks
```

```$xslt
PGSSLMODE=disable ./migrate -source file://./conf/db/migrations -database postgres://default:secret@localhost:5432/default up
```

```$xslt
dc exec migrate /migrate -source file:///migrations -database postgres://default:secret@pgsql:5432/default up
```

```$xslt
dc exec migrate /migrate create -ext sql -dir /migrations create_tasks
dc exec migrate chown -R 1000:1000 /migrations
```
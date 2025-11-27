# order-service-reactive

Reactive Order Service (Spring WebFlux + R2DBC) skeleton.

## How to open in IntelliJ
1. Download and unzip the project.
2. In IntelliJ choose *File → Open* and select the `pom.xml` file at project root.
3. Make sure JDK 17 is configured.

## Run locally (Postgres)
1. Start Postgres (example using Docker):
   ```
   docker run --name pg-orders -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=ordersdb -p 5432:5432 -d postgres:15
   ```
2. Apply schema:
   ```
   docker exec -it <container> psql -U postgres -d ordersdb -c "CREATE TABLE ..." 
   ```
   or copy the `src/main/resources/schema.sql` and run with psql:
   ```
   psql -h localhost -U postgres -d ordersdb -f src/main/resources/schema.sql
   ```
3. Run the app from IntelliJ or with:
   ```
   mvn spring-boot:run
   ```

## Notes
- This skeleton uses R2DBC url `r2dbc:postgresql://localhost:5432/ordersdb`.
- For database migrations use a migration tool compatible with R2DBC (r2dbc-migrate) or run schema.sql manually.
- Add WebClient calls to other services (inventory, sales, notification) in `OrderService` as needed.

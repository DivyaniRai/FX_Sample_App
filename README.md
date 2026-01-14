FX Trading Practice Application

Run backend:

1. Build and run with Maven:

```bash
mvn spring-boot:run
```

2. API key: include header `X-API-KEY: test-key` on requests.

Swagger UI: http://localhost:8080/swagger-ui.html
H2 console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:tradedb)

Sample cURL requests:

# Create a customer
curl -X POST -H "Content-Type: application/json" -H "X-API-KEY: test-key" -d '{"id":"CUST1","name":"Test Cust"}' http://localhost:8080/api/customers

# Create spot trade
curl -X POST -H "Content-Type: application/json" -H "X-API-KEY: test-key" -d '{"buyCurrency":"EUR","sellCurrency":"USD","buyAmount":1000,"rate":1.1,"customerId":"CUST1"}' http://localhost:8080/api/trades/spot

# Create forward trade with tenor
curl -X POST -H "Content-Type: application/json" -H "X-API-KEY: test-key" -d '{"buyCurrency":"USD","sellCurrency":"INR","sellAmount":83000,"rate":83.5,"customerId":"CUST1","tenor":"1M"}' http://localhost:8080/api/trades/forward

# Confirm trade
curl -X POST -H "X-API-KEY: test-key" http://localhost:8080/api/trades/1/confirm

# Generate confirmation
curl -X POST -H "X-API-KEY: test-key" http://localhost:8080/api/confirmations/generate?tradeId=1

# Get rate
curl -H "X-API-KEY: test-key" http://localhost:8080/api/rates/EURUSD

# Development

```bash
mvn package
docker build . -t fractalwoodstories/product-service:latest
docker push fractalwoodstories/product-service:latest
helm upgrade --install product-service ./helm/product-service
```
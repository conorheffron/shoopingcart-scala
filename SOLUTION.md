# shoppingcart-scala

[![Scala CI](https://github.com/conorheffron/shoppingcart-scala/actions/workflows/scala.yml/badge.svg)](https://github.com/conorheffron/shoppingcart-scala/actions/workflows/scala.yml)

[![SonarQube](https://github.com/conorheffron/shoppingcart-scala/actions/workflows/sonar.yml/badge.svg)](https://github.com/conorheffron/shoppingcart-scala/actions/workflows/sonar.yml)

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=conorheffron_shoppingcart-scala)](https://sonarcloud.io/summary/new_code?id=conorheffron_shoppingcart-scala)

[SonarQube Overall Code Summary](https://sonarcloud.io/summary/overall?id=conorheffron_shoppingcart-scala&branch=main)

## Tech:
 - Java 21 (LTS), SBT 1.11.6, Scala 3.7.2, ZIO 2, IntelliJ IDEA 2025.2.1 (Community Edition), SCoverage

## SBT shell build.
 - clean package test
 
![sbt-shell-clean-package-test](screenshots/sbt-shell-clean-package-test.png)

## Run Sample Cart Calculation via Spec test suite.
![shopping-cart-sample-spec](screenshots/shopping-cart-sample-spec.png)

## Main class run - prints each valid product info
![main-list-products-info](screenshots/main-list-products-info.png)

## Run Rest API Application
```
com.siriusxm.example.restApiApplication
```

### GET Request End-point (Browser Link)
```
http://localhost:8080/product/info?titles=cheerios,cornflakes,frosties,shreddies,weetabix
```

### GET Request End-point (Curl)
```
shopping-cart-scala % curl -Xcurl -XGET "http://localhost:8080/product/info?titles=cheerios,cornflakes"                            

{"cheerios":8.43,"cornflakes":2.52}
```

### GET Response JSON in Browser (key, value pairs of product_name -> price)
![GET-product-info-endpoint](screenshots/GET-product-info-endpoint.png)
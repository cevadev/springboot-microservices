mkdir microservices
cd microservices

spring init --boot-version=2.5.2 --build=gradle --java-version=11 --packaging=jar --name=product-service --package-name=ar.admiral.microservices.core.product --groupId=ar.admiral.microservices.core.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-service


spring init --boot-version=2.5.2 --build=gradle --java-version=11 --packaging=jar --name=review-service --package-name=ar.admiral.microservices.core.review --groupId=ar.admiral.microservices.core.review --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT review-service


spring init --boot-version=2.5.2 --build=gradle --java-version=11 --packaging=jar --name=recommendation-service --package-name=ar.admiral.microservices.core.recommendation --groupId=ar.admiral.microservices.core.recommendation --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT recommendation-service


spring init --boot-version=2.5.2 --build=gradle --java-version=11 --packaging=jar --name=product-composite-service --package-name=ar.admiral.microservices.composite.product --groupId=ar.admiral.microservices.composite.product --dependencies=actuator,webflux --version=1.0.0-SNAPSHOT product-composite-service

cd ..

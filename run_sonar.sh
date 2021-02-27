#!/bin/sh
# running sonar test and repors
mvn clean test
mvn sonar:sonar   -Dsonar.projectKey=sg-rail-router   -Dsonar.host.url=https://sonarqube.creators.lk  -Dsonar.login=08f60afad1c82b425621c0b6f7439fa4d70fa585 -Dsonar.coverage.jacoco.xmlReportPaths=./target/site/jacoco/jacoco.xml -Dsonar.exclusions=src/java/test/**

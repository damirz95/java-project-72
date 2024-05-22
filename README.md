### Hexlet tests and linter status:
[![Actions Status](https://github.com/damirz95/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/damirz95/java-project-72/actions)
[![Actions Status](https://github.com/damirz95/java-project-72/actions/workflows/build.yml/badge.svg)](https://github.com/damirz95/java-project-72/actions)
### CodeClimate:
[![Maintainability](https://api.codeclimate.com/v1/badges/80ea87602bf89812ac20/maintainability)](https://codeclimate.com/github/damirz95/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/80ea87602bf89812ac20/test_coverage)](https://codeclimate.com/github/damirz95/java-project-72/test_coverage)
# Java project "Page Analyzer"
## Description
#### The page analyzer is a website written in the javelin web framework.
#### The website is designed on the principles of modern MVC architecture websites: working with routing, query handlers and a template engine, interacting with the database via ORM. The main task is to analyze websites for SEO suitability.
```text
The page analyzer is a website written in the javelin web framework.
The website is designed on the principles of modern MVC architecture websites: working with routing, query handlers and a template engine, interacting with the database via ORM. 
The main task is to analyze websites for SEO suitability.
```
## How it works
```text
1-Checking that the entered link is a valid  website (containing the protocol and domain).
2-Checking that the site has been added for the first time.
3-The date of the last check and the response code are displayed on each added site
4-For each added site, you can check the availability, title, and description
```
## Requirements
#### Java 21
#### Gradle 8.7
## Start
```bash
make start
```
-[Public access](https://java-analyzer.onrender.com/) on a free server from Render
## Preview
![main page](/analyzerMain.png "main page")
![urls page](/analyzerUrls.png "urls page")
![url page](/analyzerUrl.png "url page")
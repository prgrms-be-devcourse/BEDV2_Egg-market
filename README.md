# π₯ λ¬κ± λ§μΌ

λ°±μλ μν λΉκ·Όλ§μΌ ν΄λ‘ μ½λ© νλ‘μ νΈ

---

## π§βπ€βπ§ νμ μκ°

| Scrum Master  | Product Owner| Developer | Developer | Mentor |
|------------|-----------|-----------|------|------|
| κΉμ©μ²        | μ΅μΈμ°½     | λ§μ§μ     | μ΄μ°μ°    | μ |

## νλ‘μ νΈ λͺ©ν

- λΉκ·Όλ§μΌ, μ€κ³ λλΌμ κ°μ μ€κ³ κ±°λ μμ€νμ λ§λλ κ²μ λͺ©νλ‘ μ§ννμ΅λλ€.
- κ΅¬ν λΏλ§ μλλΌ ν΄λΌμ°λ νκ²½μμ μ νλ¦¬μΌμ΄μμ CI/CDλ₯Ό ν΅ν΄ λ°°ν¬νλ κ²½νμ νλ κ²μ λͺ©νλ‘ μ§ννμ΅λλ€.

### μ νλ¦¬μΌμ΄μ νλ¦
![](./image/flow.png)

## κ°λ° μΈμ΄ λ° νμ©κΈ°μ 

### λ°±μλ 
| ![java](image/be/java.png) | ![maven](image/be/maven.png) | ![spring_boot](image/be/spring_boot.png) | ![jpa](image/be/jpa.png) | ![security](image/be/ss.png)  |
|----------------------------|------------------------------|------------------------------------------|--------------------------|-------------------------------|
| Java 17                    | maven                        | Spring Boot                              | Spring Data JPA          | Spring Security               |

| ![restdocs](image/be/restdocs.png) | ![junit](image/be/junit.png) | ![mockito](image/be/mockito.png) | ![mysql](image/be/mysql.png) |
|------------------------------------|------------------------------|----------------------------------|------------------------------|
| Spring RestDocs                    | Junit 5                      | Mockito                          | MySQL 8                      |
### DevOps
| ![actions](image/devops/actions.png) | ![ec2](image/devops/ec2.png) | ![s3](image/devops/s3.png) | ![codedeploy](image/devops/codedeploy.png) | ![rds](image/devops/rds.png) |
|--------------------------------------|------------------------------|----------------------------|--------------------------------------------|------------------------------|
| Github Actions                       | AWS EC2                      | AWS S3                     | AWS CodeDeploy                             | AWS RDS                      |

### νμν΄
| ![jira](image/work/jira.png) | ![notion](image/work/notion.png) | ![slack](image/work/slack.png) | ![github](image/work/github.png) |
|------------------------------|----------------------------------|--------------------------------|----------------------------------|
| JIRA                         | notion                           | Slack                          | Github                           |

## λΈλμΉ κ΄λ¦¬ μ λ΅
λΈλμΉ κ΄λ¦¬ μ λ΅μ github flowλ₯Ό μ¬μ©ν©λλ€.

λͺ¨λ  λΈλμΉλ Pull Requestλ₯Ό ν΅ν΄ νμμ λ¦¬λ·° ν λ©μΈμ merge ν  μ μμ΅λλ€.

![](./image/githubflow.png)
- main : feature λΈλμΉμμ κ°λ°μ΄ μλ£λμμ λ mainμΌλ‘ merge λλ©° λ°λ‘ λ°°ν¬ν  μ μλ μνμ¬μΌ ν©λλ€.
- feature : κΈ°λ₯ κ°λ°μ μ§νν  λ μ¬μ©ν©λλ€.
## μ€κ³

### ERD
![](./image/erd.png)

### μΈνλΌ κ΅¬μ‘°
![](./image/infrasturcture.png)

### API λ¬Έμ
[λ¬κ±λ§μΌ API λ¬Έμ νμΈ](http://13.124.250.199:8080/docs/index.html)

## μ€ν λ°©λ²

``` shell
    git clone https://github.com/prgrms-be-devcourse/BEDV2_Egg-market.git
    cd BEDV2_Egg-market
    mvn package
    java -jar ./target/eggmarket-0.0.1-SNAPSHOT.jar
```

## νλ‘μ νΈ νμ΄μ§
[νλ‘μ νΈ λ¬Έμ](https://www.notion.so/backend-devcourse/2-b71b5638caac4234943614a8cc103806)

### νκ³ 
[νλ‘μ νΈ νκ³ ](https://www.notion.so/backend-devcourse/36798548e8ac4eac914405e4be30c33a)
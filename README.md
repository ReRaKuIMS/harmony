# Harmony

Harmony is advanced a project on DDD.

## Core

### Entity

```scala
package domain

import jp.co.reraku.harmony.{ Identifier, Entity }
import java.util.UUID

case class PersonId(value: UUID) extends Identifier[UUID]

case class Person(id: PersonId, name: String) extends Entity[PersonId]
```

### Repository

```scala
package domain

import jp.co.reraku.harmony.Repository

trait PersonRepository[X, M[+_]] extends Repository[PersonId, Person, X, M] {
  def findByName(name: String)(implicit X): M[Person]
}
```

### RequextContext

```scala
package domain

import jp.co.reraku.harmony.RequextContext

trait ApiRequestContext extends RequestContext {
  def requireAuthenticationToken(): Option[String]
}
```

### Command

```scala
package domain

import jp.co.reraku.harmony.Command

case class RegisterPerson(id: PersonId, name: String) extends Command[RegisterPerson]

class RegisterPersonHandler() extends CommandHandler[ApiContext, RegisterPerson, Person] {
  def apply(context: ApiContext, command: RegisterPerson): Person = {
    // ...
  }
}
```

### Query

```scala
package domain

import jp.co.reraku.harmony.Query

case class LookupPerson(id: PersonId) extends Query[LookupPerson]

class LookupPersonHandler() extends QueryHandler[ApiContext, LookupPerson, Option[Person]] {
  def apply(context: ApiContext, query: LookupPerson): Option[Person] = {
    // ...
  }
}
```

## Use Scalatra as a presentation layer

### RequestContext

```scala
package presentation

import domain._

import javax.servlet.http.HttpServletRequest

class ScalatraApiRequestContext(request: HttpServletRequest) extends ApiRequestContext {

  // define the methods requested from the domain layer
  def requireAuthenticationToken(): Option[String] = {
    // ...
  }

}
```

### Inbound

```scala
package presentation

import domain._

import jp.co.reraku.harmony.scalatra.{ ScalatraInbound, ScalatraContextWrapper }

object RegisterPersonInbound extends ScalatraInbound[ApiStack, RegisterPerson] {
  def apply(scalatraContext: ScalatraContextWrapper[ApiStack]): RegisterPerson = {
    // ...
  }
}
```

### Outbound

```scala
package presentation

import domain._

import jp.co.reraku.harmony.scalatra.ScalatraOutbound
import org.scalatra._

object RegisterPersonOutbound extends ScalatraOutbound[RegisterPerson, Person] {
  def apply(command: RegisterPerson, person: Person): ActionResult = {
    // ...
  }

  def recover(command: RegisterPerson, exception: Exception): ActionResult = {
    // ...
  }
}
```

### Servlet

```scala
import domain._
import presentation._

import jp.co.reraku.harmony.scalatra.HarmonySupport
import org.scalatra._
import org.scalatra.json._

trait ApiStack extends ScalatraServlet with HarmonySupport[ApiStack, ApiRequestContext] {
  def context(): ApiRequestContext = new ScalatraApiRequestContext(request)
}

class PersonEndpoint extends ApiStack {

  // assignment inbounds and outbounds for implicit parameters
  implicit val registerPersonInbound = RegisterPersonInbound
  implicit val registerPersonOutbound = RegisterPersonOutbound
  implicit val lookupPersonInbound = LookupPersonInbound
  implicit val lookupPersonOutbound = LookupPersonOutbound

  // define route: POST /people
  arrive("/people") handleWith {
    new RegisterPersonHandler()
  }

  // define route: GET /people/:id
  arrive("/people/:id") handleWith {
    new LookupPersonHandler()
  }

}
```

## Use ScalikeJDBC as an infrastructure layer

### Repository

```scala
package infrastructure

import domain._

import scalikejdbc._
import scala.util.Try

class ScalikejdbcPersonRepository extends PersonRepository[DBSession, Try] {

  // define the methods requested from Repository
  def resolve(id: PersonId)(implicit context: DBSession): Try[Person] = {
    // ...
  }

  def store(person: Person)(implicit context: DBSession): Person = {
    // ...
  }

  // define the methods requested from the domain layer
  def findByName(name: String)(implicit context: DBSession): Try[Person] = {
    // ...
  }

}
```

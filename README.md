# Harmony

Harmony is advanced a project on DDD.

## Core

### Entity

```scala
import jp.co.reraku.harmony.{ Identifier, Entity }
import java.util.UUID

case class PersonId(value: UUID) extends Identifier[UUID]

case class Person(id: PersonId, name: String) extends Entity[PersonId]
```

### Repository

```scala
import jp.co.reraku.harmony.{ Repository, PersistenceContext }

trait PersonRepository[S, M[+_]] extends Repository[PersonId, Person, S, M] {
  def findByName(name: String)(implicit PersistenceContext[S]): M[Person]
}
```

### RequextContext (via HTTP)

```scala
import jp.co.reraku.harmony.RequextContext
import javax.servlet.http.HttpServletRequest

class ApiContext(request: HttpServletRequest) extends RequestContext {
  def requireAuthenticationToken(): Option[String] = {
    // ...
  }
}
```

### Command

```scala
import jp.co.reraku.harmony.Command

case class RegisterPerson(id: PersonId, name: String) extends Command[RegisterPerson]

class RegisterPersonHandler() extends CommandHandler[ApiContext, RegisterPerson, Person] {
  def apply(context: ApiContext, command: RegisterPerson): Person = {
    // ...
  }
}
```

### Query

```
import jp.co.reraku.harmony.Query

case class LookupPerson(id: PersonId) extends Query[LookupPerson]

class LookupPersonHandler() extends QueryHandler[ApiContext, LookupPerson, Option[Person]] {
  def apply(context: ApiContext, query: LookupPerson): Option[Person] = {
    // ...
  }
}
```

## Use Scalatra as a presentation layer

### Inbound

```scala
import jp.co.reraku.harmony.scalatra.{ ScalatraInbound, ScalatraContextWrapper }

object RegisterPersonInbound extends ScalatraInbound[ApiStack, RegisterPerson] {
  def apply(scalatraContext: ScalatraContextWrapper[ApiStack]): RegisterPerson = {
    // ...
  }
}
```

### Outbound

```scala
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
import jp.co.reraku.harmony.scalatra.HarmonySupport
import org.scalatra._
import org.scalatra.json._

trait ApiStack extends ScalatraServlet with JacksonJsonSupport with HarmonySupport[ApiStack, ApiContext] {
  def context(): ApiContext = new ApiContext(request)
}

class PersonEndpoint extends ApiStack {
  // define inbounds and outbounds for implicit parameters
  implicit val registerPersonOutbound = RegisterPersonOutbound
  implicit val registerPersonInbound = RegisterPersonInbound
  // ...

  // define route: GET /people
  arrive("/people") handleWith {
    new RegisterPersonHandler()
  }

  // define route: POST /people/:id
  arrive("/people/:id") handleWith {
    new LookupPersonHandler()
  }

}
```

## Use ScalikeJDBC as an infrastructure layer

### Repository

```scala
import jp.co.reraku.harmony.PersistenceContext
import scalikejdbc._
import scala.util.Try

class ScalikejdbcPersonRepository extends PersonRepository[DBSession, Try] {

  // define the methods requested from Repository
  def resolve(id: PersonId)(implicit context: PersistenceContext[DBSession]): Try[Person] = {
    // ...
  }

  def store(person: Person)(implicit context: PersistenceContext[DBSession]): Person = {
    // ...
  }

  // define the methods requested from the domain layer
  def findByName(name: String)(implicit context: PersistenceContext[DBSession]): Try[Person] = {
    // ...
  }

}
```

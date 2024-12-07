# JavaBank Step 07: Web Services

## Goal

Integrate a REST API with JSON into JavaBank to answer customer CRUD operations.

## Skills

By doing this assignment you will:

- Learn how to define API endpoints and handle different types of HTTP
  requests.
- Grasp the concept of data validation and its importance.
- Gain hands-on experience in testing and debugging your API using
  Postman.

## Instructions

For this assignment, you will integrate a REST API with JSON into
JavaBank to manage customer data. The API should support CRUD
operations:

- CREATE — allow clients to add new customers to the database.
- READ — enable clients to retrieve customer information.
- UPDATE — allow clients to modify customer data, such as name, email,
  address, and contact information.
- DELETE — provide the functionality to remove a customer’s record from
  the database.

### Define API Endpoints

In the table below, you’ll find the API endpoints for each CRUD
operation.

| HTTP method | Endpoint                         | Action                                          |
|-------------|----------------------------------|-------------------------------------------------|
| GET         | /customer                         | Read all customers, no account info.           |
| GET         | /customer/{id}                    | Read a specific customer.                      |
| GET         | /customer/{id}/account            | Read all accounts of a customer.               |
| GET         | /customer/{cid}/account/{aid}     | Read a specific account of a customer.         |
| POST        | /customer                         | Create a new customer.                         |
| PUT         | /customer/{id}                    | Edit an existing customer.                     |
| DELETE      | /customer/{id}                    | Delete an existing customer.                   |
| DELETE      | /customer/{cid}/account/{aid}     | Delete an account.                             |

**NOTE:** `{cid}` means “customer ID“, `{aid}` — “account ID“.

It’s a convention and common practice for the endpoints to have `/api` in
them, like `http://localhost:8080/javabank/api/customer/3`. This helps
disambiguate between endpoints intended for API requests and endpoints
intended for requests to HTML pages.

Follow this exact design and structure as they
adhere to RESTful principles and maintain consistency in naming
conventions.

**NOTE:** Some of these operations might require updating and/or adding new
business logic to the service layer or modifying other layers. Feel free to change what is needed.

### Define a REST Controller

By integrating a REST API in JavaBank, you’ll be dealing with requests
that expect a different type of response: not an HTML page, but data in
the form of a JSON string. It’s good practice to implement the handling
of those requests in a different controller — for instance, a
`RestCustomerController`.

That also means that you need to differentiate between URLs used to
interact with JavaBank on the web and URLs used to interact with its
API. The base URL for the web controllers is `/customer`. For the base
URL of the REST controllers, you could define something like
`/api/customer`.

### Example

An option for implementing the first endpoint may look something like
this, for example:

```
@RestController
@RequestMapping("/api/customer")
public class RestCustomerController {

    @RequestMapping(method = RequestMethod.GET, path = {"/", ""})
    public ResponseEntity<List<Customer>> listCustomers() {

        List<Customer> customers = customerService.list();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
```

### Test With Postman

Postman is an application with a user-friendly interface that allows developers to send requests to APIs, receive responses, and test various endpoints and functionalities.

[Here’s a great tutorial on how to use Postman to make HTTP requests and analyze responses.](https://learning.postman.com/docs/sending-requests/requests/)

You should pay special attention to the following sections:

- [Create requests](https://learning.postman.com/docs/sending-requests/create-requests/request-basics/#create-a-new-request)
- [Select request protocols](https://learning.postman.com/docs/sending-requests/create-requests/request-basics/#select-request-protocols)
- [Specify request URLs](https://learning.postman.com/docs/sending-requests/create-requests/request-basics/#specify-request-urls)
- [Select request methods](https://learning.postman.com/docs/sending-requests/create-requests/request-basics/#select-request-methods)
- [Send body data with requests](https://learning.postman.com/docs/sending-requests/create-requests/parameters/#send-body-data-with-requests)
    - [Raw data](https://learning.postman.com/docs/sending-requests/create-requests/parameters/#raw-data)

After you’ve implemented your REST API, use Postman to test its functionality thoroughly. Create test scenarios for each CRUD operation, including edge cases, and verify the responses against expected outcomes.

For instance, making a `GET` request to the `localhost:8080/api/customer` endpoint should result in a response with a `200 OK` status code with something similar to the following payload:

```
[
    {
        "id": 1,
        "firstName": "João",
        "lastName": "Oliveira",
        "email": "joaooliv@codeforall.com",
        "phone": "123456788",
        "street": "Rua do Carmo",
        "city": "Lisbon",
        "zipcode": "1000-003"
    },
    {
        "id": 2,
        "firstName": "Sara",
        "lastName": "Lopes",
        "email": "saralop@codeforall.com",
        "phone": "987654321",
        "street": "Avenida da Boavista",
        "city": "Porto",
        "zipcode": "4050-081"
    },
    {
        "id": 3,
        "firstName": "Miguel",
        "lastName": "Oliveira",
        "email": "migueloliv@codeforall.com",
        "phone": "555555555",
        "street": "Rua da Liberdade",
        "city": "Faro",
        "zipcode": "8000-295"
    },
    // [...]
]
```

And making a `GET` request to `localhost:8080/api/customer/1` should
return a payload similar to this one:

```
{
    "id": 1,
    "firstName": "João",
    "lastName": "Oliveira",
    "email": "joaooliv@codeforall.com",
    "phone": "123456788",
    "street": "Rua do Carmo",
    "city": "Lisbon",
    "zipcode": "1000-003"
}
```

But making the same request with an ID that does not correspond to an
existing customer should not return any payload, and the status code
should be `404 Not Found`.

**NOTE:** Remember, the JavaBank application must be deployed and
PostgreSQL up and running for you to be able to test anything. This time,
you’re interacting with JavaBank not through the browser, but
through another application.

### Add Maven Dependencies

Add the following dependencies on your `pom.xml`, to use the Jackson library and to help create JSON responses and accept JSON requests:

```
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
    </dependency>
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.8.0</version>
</dependency>
```

### Optional: Add New HTML

If you’re feeling up to it, you may add more functionality to existing
HTML pages and create new ones to provide a better user experience.
You can find these images on `webapp/assets/preview`.
# Products Management API

Welcome to the Products Management API! 
This API allows users to manage and search for products. It provides endpoints for saving unique products, searching products based on various criteria, and handling product operations.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)
   - [Search Products](#search-products)
   - [Save Multiple Products](#save-multiple-products)
   - [Save a Single Product](#save-a-single-product)
- [Error Handling](#error-handling)
- [Testing](#testing)

## Features

- Search products by name and price range.
- Save multiple unique products in one request.
- Save a single product with validation.
- Handle errors with appropriate status codes and messages.

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/thaliaberger/products-management-api.git
   cd products-management-api
   
2. Build and run the application:

   ```bash
   ./gradlew bootRun
   ```

## API Endpoints

### Search Products

Endpoint: `/api/products/search`

Method: `GET`

Parameters:
- `name`: The name of the product to search for.
- `minPrice`: The minimum price of the product to search for.
- `maxPrice`: The maximum price of the product to search for.

Response:
- An array of product objects that match the search criteria.

### Save Multiple Products

Endpoint: `/api/products`

Method: `POST`

Request Body:
- An array of product objects to save.

Response:
- An array of saved product objects.

### Save a Single Product           

Endpoint: `/api/products`

Method: `POST`

Request Body:
- A single product object to save.

Response:
- The saved product object.

## Error Handling    
   

Error Codes:
- `400`: Bad Request - The request is invalid or missing required fields.
- `409`: Conflict - A product with the same name and type already exists.

## Testing

To run the tests, execute the following command:

```bash
./gradlew test
```
   
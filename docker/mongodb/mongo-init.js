db = connect('mongodb://localhost/orders');
db.createUser(
{
    user: "mongoadmin",
    pwd:  "TfQM*r8Ak7@Ghd4>EL?C9q",
    roles: [{ role: "readWrite", db: "orders" }]
});
/*
  Default Customers
*/
db.customers.insertMany([
  {
    "_id": ObjectId("65c30003e6e2b520c5064d58"),
    "name": "John Doe",
    "email": "doe.john@fakemail.net"
  },
  {
    "_id": ObjectId("65c302dae6e2b520c5064fdc"),
    "name": "Jane Roe",
    "email": "emily.johnson@emailfake.com"
  }
]);
/*
  Default Items
*/
db.items.insertMany([
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff0"),
    "name": "Bag of mangos",
    "category": "Produce",
    "price": 5.0
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff1"),
    "name": "Basket of avocados",
    "category": "Produce",
    "price": 7.50
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff2"),
    "name": "Half dozen of donuts",
    "category": "Bakery",
    "price": 8.0
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff3"),
    "name": "Pack of bagels",
    "category": "Bakery",
    "price": 9.25
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff4"),
    "name": "Bag of rice",
    "category": "Pantry staples",
    "price": 6.50
  },
  {
    "_id": ObjectId("65c303d0e6e2b520c5064ff5"),
    "name": "Extra virgin olive oil",
    "category": "Pantry staples",
    "price": 12.0
  }
]);
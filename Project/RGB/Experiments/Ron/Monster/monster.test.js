// test cases for monter.js
const check = require('./monster');

/*
test('check if player hp is 2',() =>{
  expect(player.hp).toBe(2);
});
*/


test('check if randomInt(5) is greather than 5',() =>{
  expect(randomInt(5).toBeGreaterThan(5));
});

test('check if randomInt(255) is greather than 255',() =>{
  expect(randomInt(255).toBeGreaterThan(255));
});

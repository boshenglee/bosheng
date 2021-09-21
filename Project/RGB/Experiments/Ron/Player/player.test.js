const sum = require('./player');


test('check if player hp is 2',() =>{
  expect(player.hp).toBe(2);
});

test('check if player score is 10',() =>{
  expect(player.score).toBe(10);
});

test('check if player level is 1',() =>{
  expect(player.level).toBe(1);
});

test('check if player is alive',() =>{
  expect(player.alive).toBe(true);
});

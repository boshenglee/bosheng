class App extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            cars: [
                {
                    "manufacturer": "Toyota",
                    "model": "Rav4",
                    "year": 2008,
                    "stock": 3,
                    "price": 8500
                },

                {
                    "manufacturer": "Toyota",
                    "model": "Camry",
                    "year": 2009,
                    "stock": 2,
                    "price": 6500
                },

                {
                    "manufacturer": "Toyota",
                    "model": "Tacoma",
                    "year": 2016,
                    "stock": 1,
                    "price": 22000
                },

                {
                    "manufacturer": "BMW",
                    "model": "i3",
                    "year": 2012,
                    "stock": 5,
                    "price": 12000
                },

                {
                    "manufacturer": "Chevy",
                    "model": "Malibu",
                    "year": 2015,
                    "stock": 2,
                    "price": 10000
                },

                {
                    "manufacturer": "Honda",
                    "model": "Accord",
                    "year": 2013,
                    "stock": 1,
                    "price": 9000
                },

                {
                    "manufacturer": "Hyundai",
                    "model": "Elantra",
                    "year": 2013,
                    "stock": 2,
                    "price": 7000
                },

                {
                    "manufacturer": "Chevy",
                    "model": "Cruze",
                    "year": 2012,
                    "stock": 2,
                    "price": 5500
                },

                {
                    "manufacturer": "Dodge",
                    "model": "Charger",
                    "year": 2013,
                    "stock": 2,
                    "price": 16000
                },

                {
                    "manufacturer": "Ford",
                    "model": "Mustang",
                    "year": 2009,
                    "stock": 1,
                    "price": 8000
                },

            ],
            order:0, //o mean unset, 1 mean ascending , 2 mean descending
          };
    }

    renderTableData() {
      return this.state.cars.map((cars, index) => {
         const { manufacturer, model, year, stock, price } = cars //destructuring
         return (
            <tr key={index}>
               <td>{manufacturer}</td>
               <td>{model}</td>
               <td>{year}</td>
               <td>{stock}</td>
               <td>{"$"+price+".00"}</td>
               <td>
                <button
                  onClick={() => this.incrementStock(index)}>
                  Increment
                </button>
               </td>
            </tr>
         )
      })
   }

   incrementStock(index) {
     let carsCopy = [...this.state.cars]
     carsCopy[index].stock = carsCopy[index].stock +1;
     this.setState({
       cars: carsCopy,
     });
  }

  sortYear(order){

    if(order==0||order ==2){
      let sortedCars = [...this.state.cars];
      sortedCars = sortedCars.sort((a, b) => (a.year-b.year));
      this.setState({
        cars: sortedCars,
        order:1
      });
    }

    if(order==1){
      let sortedCars = [...this.state.cars];
      sortedCars = sortedCars.sort((a, b) => (b.year-a.year));
      this.setState({
        cars: sortedCars,
        order:2,
      });
    }

  }

    render() {
        return (
          <div>
            <h1 id='title'>Cars Table</h1>
            <table id='cars'>
              <thead>
                <tr>
                  <th>manufacturer</th>
                  <th>model</th>
                  <th>
                    <button class="button" type="button" onClick={() => this.sortYear(this.state.order)}>
                      year
                      </button>
                  </th>
                  <th>stock</th>
                  <th>price</th>
                  <th>Option</th>
                </tr>
              </thead>
              <tbody>
                {this.renderTableData()}
              </tbody>
            </table>
         </div>
        );
    };
}

ReactDOM.render(<App />, document.getElementById("app"));

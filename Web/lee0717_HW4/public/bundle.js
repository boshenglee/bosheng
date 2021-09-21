"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var App = function (_React$Component) {
    _inherits(App, _React$Component);

    function App(props) {
        _classCallCheck(this, App);

        var _this = _possibleConstructorReturn(this, (App.__proto__ || Object.getPrototypeOf(App)).call(this, props));

        _this.state = {
            cars: [{
                "manufacturer": "Toyota",
                "model": "Rav4",
                "year": 2008,
                "stock": 3,
                "price": 8500
            }, {
                "manufacturer": "Toyota",
                "model": "Camry",
                "year": 2009,
                "stock": 2,
                "price": 6500
            }, {
                "manufacturer": "Toyota",
                "model": "Tacoma",
                "year": 2016,
                "stock": 1,
                "price": 22000
            }, {
                "manufacturer": "BMW",
                "model": "i3",
                "year": 2012,
                "stock": 5,
                "price": 12000
            }, {
                "manufacturer": "Chevy",
                "model": "Malibu",
                "year": 2015,
                "stock": 2,
                "price": 10000
            }, {
                "manufacturer": "Honda",
                "model": "Accord",
                "year": 2013,
                "stock": 1,
                "price": 9000
            }, {
                "manufacturer": "Hyundai",
                "model": "Elantra",
                "year": 2013,
                "stock": 2,
                "price": 7000
            }, {
                "manufacturer": "Chevy",
                "model": "Cruze",
                "year": 2012,
                "stock": 2,
                "price": 5500
            }, {
                "manufacturer": "Dodge",
                "model": "Charger",
                "year": 2013,
                "stock": 2,
                "price": 16000
            }, {
                "manufacturer": "Ford",
                "model": "Mustang",
                "year": 2009,
                "stock": 1,
                "price": 8000
            }],
            order: 0 //o mean unset, 1 mean ascending , 2 mean descending
        };
        return _this;
    }

    _createClass(App, [{
        key: "renderTableData",
        value: function renderTableData() {
            var _this2 = this;

            return this.state.cars.map(function (cars, index) {
                var manufacturer = cars.manufacturer,
                    model = cars.model,
                    year = cars.year,
                    stock = cars.stock,
                    price = cars.price; //destructuring

                return React.createElement(
                    "tr",
                    { key: index },
                    React.createElement(
                        "td",
                        null,
                        manufacturer
                    ),
                    React.createElement(
                        "td",
                        null,
                        model
                    ),
                    React.createElement(
                        "td",
                        null,
                        year
                    ),
                    React.createElement(
                        "td",
                        null,
                        stock
                    ),
                    React.createElement(
                        "td",
                        null,
                        "$" + price + ".00"
                    ),
                    React.createElement(
                        "td",
                        null,
                        React.createElement(
                            "button",
                            {
                                type: "button",
                                onClick: function onClick() {
                                    return _this2.incrementStock(index);
                                } },
                            "Increment"
                        )
                    )
                );
            });
        }
    }, {
        key: "incrementStock",
        value: function incrementStock(index) {
            var carsCopy = [].concat(_toConsumableArray(this.state.cars));
            carsCopy[index].stock = carsCopy[index].stock + 1;
            this.setState({
                cars: carsCopy
            });
        }
    }, {
        key: "sortYear",
        value: function sortYear(order) {

            if (order == 0 || order == 2) {
                var sortedCars = [].concat(_toConsumableArray(this.state.cars));
                sortedCars = sortedCars.sort(function (a, b) {
                    return a.year - b.year;
                });
                this.setState({
                    cars: sortedCars,
                    order: 1
                });
            }

            if (order == 1) {
                var _sortedCars = [].concat(_toConsumableArray(this.state.cars));
                _sortedCars = _sortedCars.sort(function (a, b) {
                    return b.year - a.year;
                });
                this.setState({
                    cars: _sortedCars,
                    order: 2
                });
            }
        }
    }, {
        key: "render",
        value: function render() {
            var _this3 = this;

            return React.createElement(
                "div",
                null,
                React.createElement(
                    "h1",
                    { id: "title" },
                    "Cars Table"
                ),
                React.createElement(
                    "table",
                    { id: "cars" },
                    React.createElement(
                        "thead",
                        null,
                        React.createElement(
                            "tr",
                            null,
                            React.createElement(
                                "th",
                                null,
                                "manufacturer"
                            ),
                            React.createElement(
                                "th",
                                null,
                                "model"
                            ),
                            React.createElement(
                                "th",
                                null,
                                React.createElement(
                                    "button",
                                    { "class": "button", type: "button", onClick: function onClick() {
                                            return _this3.sortYear(_this3.state.order);
                                        } },
                                    "year"
                                )
                            ),
                            React.createElement(
                                "th",
                                null,
                                "stock"
                            ),
                            React.createElement(
                                "th",
                                null,
                                "price"
                            ),
                            React.createElement(
                                "th",
                                null,
                                "Option"
                            )
                        )
                    ),
                    React.createElement(
                        "tbody",
                        null,
                        this.renderTableData()
                    )
                )
            );
        }
    }]);

    return App;
}(React.Component);

ReactDOM.render(React.createElement(App, null), document.getElementById("app"));

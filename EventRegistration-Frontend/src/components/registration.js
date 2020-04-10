import _ from "lodash";
import axios from "axios";
let config = require("../../config");

let backendConfigurer = function() {
  switch (process.env.NODE_ENV) {
    case "testing":
    case "development":
      return "http://" + config.dev.backendHost + ":" + config.dev.backendPort;
    case "production":
      return (
        "https://" + config.build.backendHost + ":" + config.build.backendPort
      );
  }
};

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
  baseURL: backendUrl
  // headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
  name: "eventregistration",
  data() {
    return {
      persons: [],
      organizers: [],
      events: [],
      carShows: [],
      bitcoins: [],
      amount: 0,
      newPerson: "",
      personType: "Person",
      newBitcoin: {
        id: '',
        amount: '',
      },
      newEvent: {
        name: '',
        date: "2017-12-08",
        startTime: "09:00",
        endTime: "11:00",
        make: ''
      },
      selectedPerson: '',
      selectedEvent: '',
      registrationPerson: '',
      registrationEvent: '',
      paymentId: '',
      paymentAmount: '',
      assignSelectedProfessional: '',
      assignSelectedEvent: '',
      paymentPerson: '',
      paymentEvent: '',
      errorPerson: '',
      errorEvent: '',
      errorRegistration: '',
      paymentError: '',
      response: [],
    };
  },
  created: function() {
    // Initializing persons
    AXIOS.get("/persons")
      .then(response => {
        this.persons = response.data;
        this.persons.forEach(person => this.getRegistrations(person.name));
      })
      .catch(e => {
        this.errorPerson = e;
      });

      AXIOS.get("/organizers")
      .then(response => {
        this.organizers = response.data;
        this.organizers.forEach(organizer => this.getRegistrations(organizer.name));
      })
      .catch(e => {
        this.errorPerson = e;
      });

      AXIOS.get("/bitcoins")
      .then(response => {
        this.bitcoins = response.data;
      }) 
      .catch(e => {
        this.errorPerson = e;
      });


    AXIOS.get("/events") 
      .then(response => {
        this.events = response.data;
      })
      .catch(e => {
        this.errorEvent = e;
      });
  },

  methods: {
    createPerson: function(personType, personName) {
      if (personType === "Person") {
        AXIOS.post("/persons/".concat(personName), {}, {})
          .then(response => {
            this.persons.push(response.data);
            this.errorPerson = "";
            this.newPerson = "";
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorPerson = e;
            console.log(e);
          });
      } else if (personType === "Organizer") {
        AXIOS.post("/organizers/".concat(personName), {}, {})
          .then(response => {
            this.persons.push(response.data);
            this.organizers.push(response.data)
            this.errorPerson = "";
            this.newPerson = "";
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorPerson = e;
            console.log(e);
          });
      }
    },

    createEvent: function (newEvent) {
      let url = '';
      console.log("Creating event: ");
      console.log(newEvent);
      const DATE_MAX_LEN = "2999-12-31".length
      if (newEvent.date.length > DATE_MAX_LEN) {
        newEvent.date = newEvent.date.substr(newEvent.date.length - DATE_MAX_LEN);
      }
      console.log("Event has date: '" + newEvent.date + "'");

      AXIOS.post('/events/'.concat(newEvent.name), {}, {params: newEvent})
      .then(response => {
        console.log(newEvent.make);
        this.events.push(response.data);
        this.errorEvent = '';
        this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorEvent = e;
        console.log(e);
      });
      console.log(this.events);
    },

    registerEvent: function(personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name
      };

      AXIOS.post("/register", {}, { params: params })
        .then(response => {
          person.eventsAttended.push(event);
          this.selectedPerson = "";
          this.selectedEvent = "";
          this.errorRegistration = "";
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorRegistration = e;
          console.log(e);
        });
    },

    makePayment: function (personName, eventName, bitcoin, amount) {
      console.log(personName);
      console.log(eventName);
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name,
        bitcoin: bitcoin,
        amount: amount
      };

      AXIOS.post('/pay', {}, {params: params})
      .then(response => {
        person.eventsAttended.push(event)
        person.bitcoins.push(response.data)

        this.paymentPerson = '';
        this.paymentEvent = '';
        this.paymentId = '';
        this.paymentAmount = 0;
        console.log(response.data);
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.paymentError = e;
        console.log(e);
      });
    },

    getRegistrations: function(personName) {
      AXIOS.get("/events/person/".concat(personName))
        .then(response => {
          if (!response.data || response.data.length <= 0) return;

          let indexPart = this.persons.map(x => x.name).indexOf(personName);
          this.persons[indexPart].eventsAttended = [];
          response.data.forEach(event => {
            this.persons[indexPart].eventsAttended.push(event);
          });
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          console.log(e);
        });
    }
  }
};

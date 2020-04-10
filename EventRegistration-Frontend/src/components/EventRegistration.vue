<template>
  <div id="eventregistration">
    <h2>Persons</h2>
    <table id="persons-table">
      <tr>
        <th>Name</th>
        <th>Events</th>
        <th>Payment ID</th>
        <th>Amount ($)</th>
      </tr>
     <tr v-for="(person, i) in persons" v-bind:key="`person-${i}`">
        <td>{{person.name}}</td>
        <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.name}}</span>
            </li>
          </ul>
        </td>
        <td>
          <ul>
            <li v-for="(creditCard, i) in person.creditCards" v-bind:key="`event-${i}`">
              <span class='registration-event-name'>{{creditCard.accountNumber}}</span>
            </li>
          </ul>
        </td>
        <td>
          <ul>
            <li v-for="(creditCard, i) in person.creditCards" v-bind:key="`event-${i}`">
              <span class='registration-event-name'>{{creditCard.amount}}</span>
            </li>
          </ul>
        </td>
      </tr>
  <tr>
        <td>
          <input
            id="create-person-person-name"
            type="text"
            v-model="newPerson"
            placeholder="Person Name"
          />
        </td>
        <td>
          <select id="create-person-person-type" v-model="personType">
            <option disabled value="">Please select one</option>
            <option>Person</option>
            <option>Organizer</option>
          </select>
        </td>
        <td>
          <button
            id="create-person-button"
            v-bind:disabled="!newPerson"
            @click="createPerson(personType, newPerson)"
          >
            Create Person
          </button>
        </td>
        <td></td>
        <td></td>
      </tr>
    </table>

    <span v-if="errorPerson" style="color:red">Error: {{ errorPerson }}</span>

    <hr />
    <h2>Events</h2>
    <table id="events-table">
      <tr>
        <th>Name</th>
        <th>Date</th>
        <th>Start</th>
        <th>End</th>
        <th>make</th>
      </tr>
          <tr v-for="(event, i) in events" v-bind:id="event.name" v-bind:key="`event-${i}`">
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-name`">{{event.name}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-date`">{{event.date}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-starttime`">{{event.startTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-endtime`">{{event.endTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-make`">{{event.make}}</td>
      </tr>
      <tr>
        <td>
          <input id="event-name-input" type="text" v-model="newEvent.name" placeholder="Event Name">
        </td>
        <td>
          <input id="event-date-input" type="date" min="0000-00-00" max="2999-12-31" v-model="newEvent.date" placeholder="YYYY-MM-DD">
        </td>
        <td>
          <input id="event-starttime-input" type="time" v-model="newEvent.startTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-endtime-input" type="time" v-model="newEvent.endTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-make-input" type="text" v-model="newEvent.make" placeholder="make">
        </td>
        <td>
          <button id="event-create-button" v-bind:disabled="!newEvent.name" v-on:click="createEvent(newEvent)">Create</button>
        </td>
      </tr>
    </table>
    <span id="event-error" v-if="errorEvent" style="color:red">Error: {{errorEvent}}</span>
    <hr>
    <h2>Registrations</h2>
    <label
      >Person:
      <select id="registration-person-select" v-model="selectedPerson">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{
          person.name
        }}</option>
      </select>
    </label>
    <label
      >Event:
      <select id="registration-event-select" v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{
          event.name
        }}</option>
      </select>
    </label>
    <button
      id="registration-button"
      v-bind:disabled="!selectedPerson || !selectedEvent"
      @click="registerEvent(selectedPerson, selectedEvent)"
    >
      Register
    </button>
    <br />
    <span v-if="errorRegistration" style="color:red"
      >Error: {{ errorRegistration }}</span
    >
    <hr />
    <h2>Assign Professionals</h2>
    <label
      >Organizer:
      <select id="assign-selected-organizer" v-model="selectedPerson">
        <option disabled value="">Please select one</option>
        <option
          v-for="(organizer, i) in organizers"
          v-bind:key="`organizer-${i}`"
          >{{ organizer.name }}</option
        >
      </select>
    </label>
    <label
      >Event:
      <select id="assign-selected-event-organizer" v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{
          event.name
        }}</option>
      </select>
    </label>
    <button
      id="assign-button-organizer"
      v-bind:disabled="!selectedPerson || !selectedEvent"
      @click="registerEvent(selectedPerson, selectedEvent)"
    >
      Assign
    </button>
    <br />
    <span v-if="errorRegistration" style="color:red"
      >Error: {{ errorRegistration }}</span
    >
    <hr />
 <h2>Pay for Registration with Bitcoin</h2>
    <label>Person:
      <select id='bitcoin-person-select' v-model="paymentPerson">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{person.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='bitcoin-event-select' v-model="paymentEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
      </select>
    </label>
    <label>Device Id:
        <input id='bitcoin-id-input' type="text" placeholder='' v-model="paymentId">
    </label>
    <label>Amount:
        <input id="bitcoin-amount-input" type="number" v-model="paymentAmount">
    </label>
    <button id='bitcoin-button' v-bind:disabled="!paymentPerson || !paymentEvent || !paymentId || !paymentAmount" @click="makePayment(paymentPerson, paymentEvent, paymentId, paymentAmount)">Make payment</button>
    <br/>
    <span id="bitcoin-error" v-if="paymentError" style="color:red">Error: {{paymentError}}</span>
    <hr>
  </div>
</template>

<script src="./registration.js"></script>

<style>
#eventregistration {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background: #f2ece8;
  margin-top: 60px;
}
.registration-event-name {
  display: inline-block;
  width: 25%;
}
.registration-event-name {
  display: inline-block;
}
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  text-align: left;
}
a {
  color: #42b983;
}
</style>

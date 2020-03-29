/* eslint-disable semi */
/* eslint-disable indent */
/* eslint-disable no-trailing-spaces */
var config = require('./config.json')

var testData = {
  "name1": "elias",
  "name2": "abbas",
  "name3": "marton",
  "name4": "john",
  "eventName": "sampleEvent"
}

module.exports = {
  'Test existance of all elements': function (client) {
    client
      .url(config.url)
      .waitForElementVisible('body', 1000)
      .assert.visible(config.componentIDs.personButton)
      .assert.visible(config.componentIDs.registrationTable)
      .assert.visible(config.componentIDs.assignSelectedProfessional)
      .assert.visible(config.componentIDs.assignSelectedEvent)
      .assert.visible(config.componentIDs.assignButton)
      .assert.visible(config.componentIDs.createPersonType)
      .assert.visible(config.componentIDs.personField)
  },
  'Test Creating Person': function (client) {
    client
      .url(config.url)
      .waitForElementVisible('body', 1000)
      .assert.visible(config.componentIDs.personField)
      .setValue(config.componentIDs.personField, testData.name4)
      .pause(1000)
      .assert.visible(config.componentIDs.createPersonType)
      .setValue(config.componentIDs.createPersonType, 'Person')
      .pause(1000)
      .click(config.componentIDs.personButton)
      .pause(1000)
      .assert.containsText(config.componentIDs.registrationTable, testData.name4)
      .end();
  },
  'Test Creating Professional': function (client) {
    client
      .url(config.url)
      .waitForElementVisible('body', 1000)
      .assert.visible(config.componentIDs.personField)
      .setValue(config.componentIDs.personField, testData.name1 + config.professional)
      .pause(1000)
      .assert.visible(config.componentIDs.createPersonType)
      .setValue(config.componentIDs.createPersonType, config.professional)
      .pause(1000)
      .click(config.componentIDs.personButton)
      .pause(1000)
      .assert.containsText(config.componentIDs.registrationTable, testData.name1 + config.professional)
      .end();
  },
  'Test Creating 2 Professional': function (client) {
    client
      .url(config.url)
      .waitForElementVisible('body', 1000)
      .assert.visible(config.componentIDs.personField)
      .setValue(config.componentIDs.personField, testData.name2 + config.professional)
      .pause(1000)
      .assert.visible(config.componentIDs.createPersonType)
      .setValue(config.componentIDs.createPersonType, config.professional)
      .pause(1000)
      .click(config.componentIDs.personButton)
      .pause(1000)
      .assert.containsText(config.componentIDs.registrationTable, testData.name2 + config.professional)
      .pause(1000)
      .setValue(config.componentIDs.personField, testData.name3 + config.professional)
      .setValue(config.componentIDs.createPersonType, config.professional)
      .click(config.componentIDs.personButton)
      .pause(1000)
      .assert.containsText(config.componentIDs.registrationTable, testData.name3 + config.professional)
      .end();
  },
  'Test Create a sample event for next tests': function (client) {
    client
      .url(config.url)
      .waitForElementVisible('body', 1000)
      .setValue(config.componentIDs.eventField, testData.eventName + config.professional)
      .pause(1000)
      .setValue(config.componentIDs.eventDate, "12/04/2019")
      .setValue(config.componentIDs.eventStartTime, "8:00 AM")
      .setValue(config.componentIDs.eventEndTime, "9:00 AM")
      .pause(1000)
      .click(config.componentIDs.eventButton)
  },
  'Test Assign a Professional to an Event': function (client) {
    client
      .url(config.url)
      .waitForElementVisible('body', 1000)
      .assert.visible(config.componentIDs.assignSelectedProfessional)
      .assert.visible(config.componentIDs.assignSelectedEvent)
      .assert.visible(config.componentIDs.assignButton)
      .setValue(config.componentIDs.assignSelectedProfessional, testData.name1 + config.professional)
      .pause(1000)
      .setValue(config.componentIDs.assignSelectedEvent, testData.eventName + config.professional) //adding the professional to make sure that hte event name is unique
      .pause(1000)
      .click(config.componentIDs.assignButton)
      .pause(1000)
      .end();
  },
};

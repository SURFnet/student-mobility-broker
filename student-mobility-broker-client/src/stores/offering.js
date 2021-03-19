import {writable} from 'svelte/store';

export const offering = writable({
  homeInstitution: {},
  guestInstitution: {
    abbreviation: "Guest institution"
  },
  offering: {},
  correlationID: ""
});

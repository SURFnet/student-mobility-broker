import {writable} from 'svelte/store';

export const config = writable({
  local: false,
  allowPlayground: false,
  pollEnabled: false,
  pollSurvey: null
});

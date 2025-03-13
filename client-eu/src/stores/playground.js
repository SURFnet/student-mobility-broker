import {writable} from 'svelte/store';

const createPlayground = () => {
  const stateString = JSON.parse(localStorage.getItem("playground")) || {
    active: false,
    code: 200,
    redirect: null,
    message: null
  };
  const {subscribe, set} = writable(stateString);

  subscribe(value => {
    localStorage.setItem("playground", JSON.stringify(value));
  });

  return {
    subscribe,
    start: (code = 200, redirect = null, message = null) => {
      set({active: true, code, redirect, message});
    },
    reset: () => {
      set({active: false, code: 200, redirect: null, message: null});
    }
  }
};
export const playground = createPlayground();

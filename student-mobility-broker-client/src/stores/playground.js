import {writable} from 'svelte/store';

const createPlayground = () => {
  const {subscribe, set} = writable({
    active: false,
    code: 200,
    redirect: null,
    message: null
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

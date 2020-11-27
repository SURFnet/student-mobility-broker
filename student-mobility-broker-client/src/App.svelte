<script>
  import Footer from "./components/Footer.svelte";
  import {Route, Router} from "svelte-routing";
  import {onMount} from "svelte";
  import Cookies from "js-cookie";
  import NotFound from "./routes/NotFound.svelte";
  import Course from "./routes/Course.svelte";
  import Header from "./components/Header.svelte";
  import {broker, features, selectedOffering} from "./api";
  import {config} from "./stores/config";
  import {offering} from "./stores/offering";
  import I18n from "i18n-js";
  import {getParameterByName} from "./utils/queryParameters";

  export let url = "";
  let loaded = false;

  if (typeof window !== "undefined") {
    const urlSearchParams = new URLSearchParams(window.location.search);
    if (urlSearchParams.has("lang")) {
      I18n.locale = urlSearchParams.get("lang");
    } else if (Cookies.get("lang", {domain: $config.domain})) {
      I18n.locale = Cookies.get("lang", {domain: $config.domain});
    } else {
      I18n.locale = navigator.language.toLowerCase().substring(0, 2);
    }
  } else {
    I18n.locale = "en";
  }
  if (["nl", "en"].indexOf(I18n.locale) < 0) {
    I18n.locale = "en";
  }

  onMount(() => {
    features().then(json => {
      $config = json;
      const init = getParameterByName("init") === "true";
      if ($config.local && !init) {
        broker("utrecht.nl", "eindhoven.nl", "1");
      } else {
        selectedOffering().then(json => {
          $offering = json;
          loaded = true;
        });
      }

    });

  });

</script>

<style>

    :global(:root) {
        --color-primary-blue: #0062b0;
        --color-primary-green: #008738;
        --color-secondary-green: #015e22;
        --color-primary-black: #202020;
        --color-primary-red: #bd0202;
        --color-primary-grey: #f7f8f7;
        --color-background: #f9f9f9;
        --width-app: 1024px;
    }

    .education {
        display: flex;
        flex-direction: column;
        height: 100%;
        margin-bottom: 100px;
    }

    .container {
        max-width: var(--width-app);
        margin: 0 auto;
        width: 100%;
        display: flex;
        flex-direction: column;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
    }

    .content {
        display: flex;
        flex-direction: column;
        background-color: white;
        align-items: stretch;
        max-width: var(--width-app);
        width: 100%;
        margin: 0 auto;
    }

    @media (max-width: 1250px) {
        .education {
            margin: 0 15px;
        }

        .content {
            width: 100%;
        }
    }

    @media (max-width: 800px) {
        .education {
            margin: 0;
        }

        .content {
            box-shadow: none;
        }
    }

    .loader:empty,
    .loader:empty:after {
        border-radius: 50%;
        width: 10em;
        height: 10em;
    }

    .loader:empty {
        margin: 60px auto;
        font-size: 10px;
        position: relative;
        text-indent: -9999em;
        border: 1.1em solid white;
        border-top-color: var(--color-primary-green);
        border-bottom-color: var(--color-primary-blue);
        -webkit-transform: translateZ(0);
        -ms-transform: translateZ(0);
        transform: translateZ(0);
        -webkit-animation: load8 1.5s infinite linear;
        animation: load8 1.5s infinite linear;
    }

    @-webkit-keyframes load8 {
        0% {
            -webkit-transform: rotate(0deg);
            transform: rotate(0deg);
        }
        100% {
            -webkit-transform: rotate(360deg);
            transform: rotate(360deg);
        }
    }

    @keyframes load8 {
        0% {
            -webkit-transform: rotate(0deg);
            transform: rotate(0deg);
        }
        100% {
            -webkit-transform: rotate(360deg);
            transform: rotate(360deg);
        }
    }
</style>
{#if loaded}
    <div class="education">
        <div class="container">
            <div class="content">
                <Router url="{url}">
                    <Route path="/" component={Course}/>
                    <Route component={NotFound}/>
                </Router>
            </div>
        </div>
        <Footer/>
    </div>
{:else}
    <div class="loader"></div>
{/if}

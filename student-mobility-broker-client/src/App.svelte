<script>
  import {navigate, Route, Router} from "svelte-routing";
  import {onMount} from "svelte";
  import Cookies from "js-cookie";
  import NotFound from "./routes/NotFound.svelte";
  import Offering from "./routes/Offering.svelte";
  import PlayGround from "./routes/PlayGround.svelte";
  import {broker, features, selectedOffering} from "./api";
  import {offering} from "./stores/offering";
  import {config} from "./stores/config";
  import I18n from "i18n-js";
  import {getParameterByName, replaceQueryParameter} from "./utils/queryParameters";
  import data from "./data/offering.json";
  import Loading from "./components/Loading.svelte";

  export let url = "";
  let loaded = false;

  if (typeof window !== "undefined") {
    const urlSearchParams = new URLSearchParams(window.location.search);
    if (urlSearchParams.has("lang")) {
      I18n.locale = urlSearchParams.get("lang");
    } else if (Cookies.get("lang")) {
      I18n.locale = Cookies.get("lang");
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
      const step = getParameterByName("step");
      const playGround = window.location.pathname.indexOf("play") > -1;
      const error = getParameterByName("error");
      const landing = getParameterByName("landing");
      if (error || landing) {
        loaded = true;
      } else if (!step && !playGround && json.allowPlayground) {
        //Mock the call from catalog to broker to ensure there is a selected offering
        broker($config.playHomeInstitutionSchacHome,
               $config.playGuestInstitutionSchacHome,
               $config.offeringID,
               "course",
               $config.startBrokerEndpoint);
      } else if (!playGround && step) {
        selectedOffering()
          .then(json => {
            $offering = json;
            loaded = true;
          })
          .catch(e => {
            if (e.status === 400) {
              e.json().then(res => {
                window.location.search = replaceQueryParameter("error", I18n.t("error.offering", {"name": res.message}));
              });
            } else {
              window.location.search = replaceQueryParameter("error", I18n.t("error.expired"));
            }
          });
      } else if (playGround && json.allowPlayground) {
        $offering = data;
        loaded = true;
      } else if (!landing) {
        window.location.search = replaceQueryParameter("landing", "true");
      } else {
        navigate("/404");
        loaded = true;
      }
    });
  });

</script>

<style global lang="scss">
  @import "stylesheets/main";

  .broker {
    display: flex;
    flex-direction: column;
    height: 100%;
    position: relative;
  }

</style>
{#if loaded }
    <div class="broker">
        <Router url="{url}">
            <Route path="/" component={Offering}/>
            {#if $config.allowPlayground}
                <Route path="/intake">
                    <PlayGround bookmark="intake"/>
                </Route>
                <Route path="/results">
                    <PlayGround bookmark="results"/>
                </Route>
                <Route path="/resultsV4">
                    <PlayGround bookmark="resultsV4"/>
                </Route>
                <Route path="/person">
                    <PlayGround bookmark="person"/>
                </Route>
            {/if}
            <Route component={NotFound}/>
        </Router>
    </div>
{:else}
    <Loading/>
{/if}

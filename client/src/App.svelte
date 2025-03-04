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
    let offeringError = null;


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
    if (!["nl", "en"].includes(I18n.locale)) {
        I18n.locale = "en";
    }

    onMount(() => {
        doOnMount();
    });

    const doOnMount = () => {
        features().then(json => {
            $config = json;
            const redirect = getParameterByName("q");
            if (redirect) {
                const decodedRedirect = decodeURIComponent(redirect);
                if (decodedRedirect.startsWith(json.queue)) {
                    window.location.href = decodedRedirect;
                    return;
                } else {
                    throw new Error("Invalid queue");
                }
            }
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
                    $config.offeringId,
                    "course",
                    $config.startBrokerEndpoint);
            } else if (!playGround && step) {
                selectedOffering()
                    .then(json => {
                        $offering = json;
                        loaded = true;
                    })
                    .catch(e => {
                        e.json().then(res => {
                            let message = res.message;
                            if (message.indexOf("session")) {
                                offeringError = I18n.t("error.412");
                            } else {
                                offeringError = I18n.t("error.offering", {"name": res.message});
                            }
                            loaded = true;
                        }).catch(() => {
                            offeringError = I18n.t("error.expired");
                            loaded = true;
                        });
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
    }

</script>

<style global lang="scss">
    :global(:root) {
        /*Colors*/
        --color-primary-blue: #225fb1;
        --color-secondary-blue: #eaf1f6;
        --color-button-blue: #1279c5;

        --color-primary-grey: #bfbfbf;
        --color-secondary-grey: #cecccc;
        --color-tertiary-grey: #8e8a8a;
        --color-grey-background: #f5f7f8;
        --color-primary-green: #008738;
        --color-primary-red: #bd0202;

    }

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
            <Route path="/">
                <Offering offeringError={offeringError}/>
            </Route>
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

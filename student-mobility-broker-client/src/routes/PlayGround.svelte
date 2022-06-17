<script>
    import {offering} from "../stores/offering";
    import {playground} from "../stores/playground";
    import {onMount} from "svelte";
    import I18n from "i18n-js";
    import {serviceRegistry} from "../api";
    import Intake from "./Intake.svelte";
    import Results from "./Results.svelte";
    import Person from "./Person.svelte";
    import {navigate} from "svelte-routing";
    import Loading from "../components/Loading.svelte";

    const responses = [
        {value: 200, label: "200 - All is good"},
        {value: 500, label: "500 - Not so good"}
    ];

    export let bookmark = "intake";
    const tabs = [
        {name: "intake", component: Intake},
        {name: "results", component: Results},
        {name: "person", component: Person},
    ];
    let currentTab = tabs[0];

    let institutions = [];
    let loaded = false;

    onMount(() => {
        playground.reset();
        serviceRegistry().then(json => {
            institutions = json.map(o => ({
                ...o,
                value: `${o.name} (${o.courseAuthentication.toLowerCase()} authentication)`,
                label: `${o.name} (${o.courseAuthentication.toLowerCase()} authentication)`
            }));
            $offering.homeInstitution = institutions[0];
            $offering.guestInstitution = institutions[1];
            currentTab = bookmark ? currentTab = tabs.find(tab => tab.name === bookmark) : tabs[0];
            loaded = true;
        });
    });

    const switchTab = name => () => navigate(`/${name}`);

</script>

<style lang="scss">

    .container {
        max-width: 720px;
        margin: 40px auto;
        width: 100%;
        display: flex;
        flex-direction: column;

        @media (max-width: 780px) {
            padding: 0 20px;
        }

        .tabs {
            display: flex;

            ul {
                list-style: none;

                li {
                    display: inline-block;
                }
            }

            a.back {
                margin-left: auto;
            }

            .tab {
                flex: 1;
                padding: 20px 40px;
                border-top-width: 2px;
                border-top-style: solid;
                cursor: pointer;

                &:not(.active) {
                    background: #f5f5f5;
                    border-color: #f5f5f5;
                    border-right: 3px solid white;

                    h3 {
                        color: #565656;
                    }
                }

                &.active {
                    background: white;
                    border-color: var(--color-primary-blue);

                    h3 {
                        color: var(--color-primary-blue);
                        font-weight: bold;
                    }
                }

                &:last-child {
                    border-right: none;
                }
            }

        }


    }


</style>
<div class="container">
    <div class="tabs">
        <ul>
            {#each tabs as tab}
                <li class="tab" class:active={tab.name === currentTab.name}
                    on:click={switchTab(tab.name)}>
                    <h3> {I18n.t(`tabs.${tab.name}`)} </h3>
                </li>
            {/each}
        </ul>
        <a class="back" href="/">Back to enrollment</a>
    </div>
    <div class="component-container">
        {#if loaded}
            <svelte:component this={currentTab.component} institutions={institutions}/>
        {:else}
            <Loading/>
        {/if}
    </div>
</div>


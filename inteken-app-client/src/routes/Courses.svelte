<script>
    import {user} from "../stores/user";
    import {onMount} from "svelte";
    import I18n from "i18n-js";
    import {navigate} from "svelte-routing";
    import Button from "../components/Button.svelte";
    import {institutions} from "../api";
    import chevron_right from "../icons/chevron-right.svg";
    let courses = [];

    onMount(() => {
        institutions().then(res => {
            courses = res.reduce((acc, val) => {
                const courses = val.courses.map(course => {
                    course.institution = val;
                    return course;
                });
                delete val.courses;
                return acc.concat(courses)
            }, []);
        });
    });

    const courseDetails = course => () =>
            navigate(`/course?identifier=${course.identifier}&schacHome=${encodeURIComponent(course.institution.schacHome)}`);

</script>

<style>
    .courses {
        width: 100%;
        height: 100%;
    }

    .inner {
        height: 100%;
        margin: 0 auto;
        padding: 15px 30px 15px 0;
        display: flex;
        flex-direction: column;
    }

    h2 {
        margin: 20px 0 10px 0;
        color: var(--color-primary-green);
    }

    p.info {
        font-weight: 300;
        margin-bottom: 26px;
    }

    p {
        font-size: 18px;
        line-height: 1.33;
        letter-spacing: normal;
    }

    table {
        margin-top: 30px;
        width: 100%;
    }

    tr {
        cursor: pointer;
    }

    td {
        border-bottom: 1px solid var(--color-primary-grey);
    }

    td.value {
        width: 70%;
        padding: 5px 5px 5px 20px;
    }

    div.value-container {
        display: flex;
        font-weight: bold;
        align-items: center;
    }

    div.description {
        padding-right: 70px;
        margin-bottom: 5px;
    }

    div.value-container a.menu-link {
        margin-left: auto;
    }

    :global(a.menu-link svg) {
        fill: var(--color-primary-green);
    }


</style>


<div class="courses">
    <div class="inner">
        <h2>{I18n.t("courses.title")}</h2>
        <p class="info">{I18n.t("courses.info")}</p>
        <table cellspacing="0">
            <thead></thead>
            <tbody>
            {#each courses as course}
                <tr class="name" on:click={courseDetails(course)}>
                    <td class="value">
                        <div class="value-container">
                            <span>{@html I18n.t("courses.link", {name: course.name, institution: course.institution.name})}</span>
                            <a class="menu-link" href="/details"
                               on:click|preventDefault|stopPropagation={courseDetails(course)}>
                                {@html chevron_right}
                            </a>
                        </div>
                        <div class="description">
                            <span>{course.description}</span>
                        </div>
                    </td>
                </tr>
            {/each}
            </tbody>
        </table>

    </div>

</div>


<script>
    export let className = "";
    export let active = false;
    export let href = "/";
    export let disabled = false;
    export let label;
    export let onClick;
    export let icon;
    export let small = false;
    export let cancel = false;

    const handleLinkClick = e => e.key === " " && e.target.click();

</script>
<style lang="scss">
    .button {
        padding: 10px 20px;
        background-color: var(--color-button-blue);
        border-radius: 4px;
        color: white;
        text-decoration: none;
        cursor: pointer;
        text-align: center;
        font-weight: 600;
        width: 280px;

        @media (max-width: 780px) {
            width: 100%;
        }

        &.small {
            width: 140px;
        }

        &.icon {
            padding: 4px 16px 4px 4px;
            display: flex;
            align-content: center;
            align-items: center;
        }

        &:hover {
            background-color: var(--color-primary-blue);
            color: whitesmoke;
        }

        &.cancel {
            background-color: white;
            color: #5d6872;
            border: 1px solid #5d6872;

            &:hover {
                background-color: #f8f8f8;
                color: black;
            }

        }

        &.disabled {
            cursor: not-allowed;
            color: #ababab;
            background-color: #efefef;
        }

        &.disabled:hover {
            color: #ababab;
            background-color: #efefef;
        }

        :global(svg) {
            width: 62px;
            margin-right: 12px;
        }

        :global(svg.eduid) {
            width: 32px;
            height: auto;
            border-radius: 4px;
        }

    }


</style>

<a class={`button ${className} ${icon ? " icon" : ""}`}
   class:small
   class:cancel
   class:active={active}
   href="{href}"
   class:disabled={disabled}
   on:click|preventDefault|stopPropagation={() => !disabled && onClick()}
   on:keydown={handleLinkClick}>
    {#if icon}
        {@html icon}
    {/if}
    <span>{label}</span>
</a>
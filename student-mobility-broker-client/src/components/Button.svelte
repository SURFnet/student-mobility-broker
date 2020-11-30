<script>
  export let className = "";
  export let active = false;
  export let href = "/";
  export let disabled = false;
  export let label;
  export let onClick;
  export let icon;

  const handleLinkClick = e => e.key === " " && e.target.click();

</script>
<style lang="scss">
  .button {

    background-color: var(--color-button-blue);
    border-radius: 8px;
    padding: 2px 16px 2px 16px;
    display: flex;
    align-content: center;
    align-items: center;
    color: white;
    text-decoration: none;
    cursor: pointer;
    text-align: center;
    font-weight: bold;
    width: 280px;

    &:hover {
      background-color: var(--color-primary-blue);
      color: whitesmoke;
    }

    :global(svg) {
      width: 62px;
      margin-right: 12px;
    }

  }


</style>

<a class="{`button ${className}`}"
   class:active={active}
   href="{href}"
   class:disabled={disabled}
   on:click|preventDefault|stopPropagation={() => !disabled && onClick()}
   on:keydown={handleLinkClick}>
    {#if icon}
        <span>{@html icon}</span>
    {/if}
    {label}
</a>
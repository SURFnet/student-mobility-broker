<script>
  import I18n from "i18n-js";
  import Button from "./Button.svelte";

  export let cancel;
  export let title;

  const handle_keydown = e => {
    if (e.key === "Escape") {
      cancel();
    }
  };

</script>

<style lang="scss">
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 57, 128, 0.8);
    z-index: 999;
    display: flex;
  }

  .modal-content {
    margin: auto;
    width: calc(100vw - 4em);
    max-width: 32em;
    max-height: calc(100vh - 4em);
    overflow-y: scroll;
    border-radius: 8px;
    background: white;
  }

  @media (max-height: 600px) {
    .modal-content {
      max-height: 380px;
    }

  }


  .modal-header {
    padding: 18px 32px;
    background-color: var(--color-button-blue);
    color: white;
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
  }

  .modal-body {
    padding: 18px 32px 0 18px;
  }

  div.options {
    display: flex;
    justify-content: flex-end;
    padding: 0 18px 18px 18px;
    text-align: center;
    margin-bottom: 15px;
  }
</style>

<svelte:window on:keydown={handle_keydown}/>

<div class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>{title}</h3>
        </div>

        <div class="modal-body">
            <slot></slot>
        </div>

        <div class="options">
            <Button className="cancel" onClick={cancel} small={true}
                    label={I18n.t("modal.ok")}/>
        </div>
    </div>
</div>

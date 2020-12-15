<script>
    import I18n from "i18n-js";
    import {LottiePlayer} from '@lottiefiles/svelte-lottie-player';
    import notFoundLottie from "../lotties/not_found.json";

</script>

<style>
    .not-found {
        display: flex;
        flex-direction: column;
        padding: 25px;
        align-content: center;
        align-items: center;
    }

</style>
<div class="not-found">
        <LottiePlayer
                src={notFoundLottie}
                autoplay="{true}"
                loop="{true}"
                controls="{false}"
                renderer="svg"
                background="transparent"
                height="{350}"
                width="{350}"
                controlsLayout={null}
        />
</div>